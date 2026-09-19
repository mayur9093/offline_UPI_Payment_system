package com.mayur.offline_UPI_system.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.mayur.offline_UPI_system.model.OfflineTransaction;
import com.mayur.offline_UPI_system.model.OfflineTransactionStatus;
import com.mayur.offline_UPI_system.repository.OfflineTransactionRepository;
import com.mayur.offline_UPI_system.repository.UserRepository;
import com.mayur.offline_UPI_system.repository.WalletRepository;
import com.mayur.offline_UPI_system.dto.OfflinePaymentRequest;
import com.mayur.offline_UPI_system.dto.OfflineSyncRequest;
import com.mayur.offline_UPI_system.exception.InsufficientBalanceException;
import com.mayur.offline_UPI_system.exception.InvalidAmountException;
import com.mayur.offline_UPI_system.exception.UserNotFoundException;
import com.mayur.offline_UPI_system.exception.WalletNotFoundException;
import com.mayur.offline_UPI_system.model.User;
import com.mayur.offline_UPI_system.model.Wallet;

import jakarta.transaction.Transactional;

@Service
public class OfflinePaymentService {
        private final UserRepository userRepository;
        private final WalletRepository walletRepository;
        private final OfflineTransactionRepository offlineTransactionRepository;
        private final CryptoService cryptoService;

        public OfflinePaymentService(UserRepository userRepository, WalletRepository walletRepository,
                        OfflineTransactionRepository offlineTransactionRepository, CryptoService cryptoService) {
                this.offlineTransactionRepository = offlineTransactionRepository;
                this.userRepository = userRepository;
                this.walletRepository = walletRepository;
                this.cryptoService = cryptoService;
        }

        @Transactional
        public OfflineTransaction createOfflinepayment(int senderId, OfflinePaymentRequest OfflinePaymentRequest) {

                User sender = userRepository.findById(senderId)
                                .orElseThrow(() -> new UserNotFoundException("Sender not Found " + senderId));

                User receiver = userRepository.findByUpiId(OfflinePaymentRequest.getReceiverUpiId())
                                .orElseThrow(() -> new UserNotFoundException(
                                                "Receiver UPI Id not found "
                                                                + OfflinePaymentRequest.getReceiverUpiId()));

                if (sender.getId() == receiver.getId()) {
                        throw new InvalidAmountException("Sender and reviver cannot be same");
                }

                BigDecimal amount = OfflinePaymentRequest.getAmount();

                if (amount == null ||
                                amount.compareTo(BigDecimal.ZERO) <= 0) {

                        throw new InvalidAmountException(
                                        "Amount must be greater than zero");
                }

                Wallet wallet = walletRepository
                                .findByUserId(senderId)
                                .orElseThrow(() -> new WalletNotFoundException(
                                                "Wallet not found for sender: "
                                                                + senderId));

                if (wallet.getBalance().compareTo(amount) < 0) {
                        throw new InsufficientBalanceException(
                                        "Insufficient balance");
                }

                String transactionReference = "OFF-" + UUID.randomUUID();

                String nonce = UUID.randomUUID().toString();

                LocalDateTime createdAt = LocalDateTime.now();

                String dataToSign = transactionReference + "|" + senderId + "|" + receiver.getId() + "|" +
                                amount.toPlainString() + "|" + createdAt + "|" + nonce;

                String signature = cryptoService.sign(dataToSign);

                OfflineTransaction transaction = new OfflineTransaction();

                transaction.setTransactionReference(transactionReference);
                transaction.setSenderId(senderId);
                transaction.setReceiverId(receiver.getId());
                transaction.setAmount(amount);
                transaction.setStatus(OfflineTransactionStatus.PENDING);
                transaction.setCreatedAt(createdAt);
                transaction.setNonce(nonce);
                transaction.setSignature(signature);

                return offlineTransactionRepository.save(transaction);

        }

        @Transactional
        public OfflineTransaction syncOfflinePayment(OfflineSyncRequest request) {

                OfflineTransaction offlineTransaction = offlineTransactionRepository
                                .findByTransactionReference(request.getTransactionReference()).orElse(null);

                if (offlineTransaction == null) {

                        offlineTransaction = new OfflineTransaction();

                        offlineTransaction.setTransactionReference(request.getTransactionReference());
                        offlineTransaction.setSenderId(request.getSenderId());
                        offlineTransaction.setReceiverId(request.getReceiverId());
                        offlineTransaction.setAmount(request.getAmount());
                        offlineTransaction.setCreatedAt(request.getCreatedAt());
                        offlineTransaction.setNonce(request.getNonce());
                        offlineTransaction.setSignature(request.getSignature());
                        offlineTransaction.setStatus(OfflineTransactionStatus.PENDING);

                        return offlineTransactionRepository.save(offlineTransaction);
                }

                if (offlineTransaction.getStatus() == OfflineTransactionStatus.SYNCED) {

                        throw new RuntimeException(
                                        "Transaction is already synced");
                }

                if (offlineTransaction.getSenderId() != request.getSenderId()) {

                        throw new RuntimeException(
                                        "Sender does not match");
                }

                if (offlineTransaction.getReceiverId() != request.getReceiverId()) {

                        throw new RuntimeException(
                                        "Receiver does not match");
                }

                if (offlineTransaction.getAmount()
                                .compareTo(request.getAmount()) != 0) {

                        throw new RuntimeException(
                                        "Amount does not match");
                }

                String dataToVerify = request.getTransactionReference() + "|" +
                                request.getSenderId() + "|" +
                                request.getReceiverId() + "|" +
                                request.getAmount().toPlainString() + "|" +
                                request.getCreatedAt() + "|" +
                                request.getNonce();

                boolean validSignature = cryptoService.verify(
                                dataToVerify,
                                request.getSignature());

                if (!validSignature) {

                        offlineTransaction.setStatus(
                                        OfflineTransactionStatus.FAILED);

                        offlineTransactionRepository.save(offlineTransaction);

                        throw new RuntimeException(
                                        "Invalid offline payment signature");
                }

                Wallet senderWallet = walletRepository
                                .findByUserId(
                                                request.getSenderId())
                                .orElseThrow(() -> new WalletNotFoundException(
                                                "Sender wallet not found"));

                Wallet receiverWallet = walletRepository
                                .findByUserId(
                                                request.getReceiverId())
                                .orElseThrow(() -> new WalletNotFoundException(
                                                "Receiver wallet not found"));

                if (senderWallet.getBalance()
                                .compareTo(request.getAmount()) < 0) {

                        offlineTransaction.setStatus(
                                        OfflineTransactionStatus.FAILED);

                        offlineTransactionRepository.save(offlineTransaction);

                        throw new InsufficientBalanceException(
                                        "Insufficient balance");
                }

                senderWallet.setBalance(
                                senderWallet.getBalance()
                                                .subtract(request.getAmount()));

                receiverWallet.setBalance(
                                receiverWallet.getBalance()
                                                .add(request.getAmount()));

                walletRepository.save(senderWallet);
                walletRepository.save(receiverWallet);

                offlineTransaction.setStatus(
                                OfflineTransactionStatus.SYNCED);

                return offlineTransactionRepository.save(
                                offlineTransaction);
        }

}
