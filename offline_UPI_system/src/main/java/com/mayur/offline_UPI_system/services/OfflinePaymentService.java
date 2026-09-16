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

        public OfflinePaymentService(UserRepository userRepository, WalletRepository walletRepository,
                        OfflineTransactionRepository offlineTransactionRepository) {
                this.offlineTransactionRepository = offlineTransactionRepository;
                this.userRepository = userRepository;
                this.walletRepository = walletRepository;
        }

        @Transactional
        public OfflineTransaction createOfflinepayment(int senderId, OfflinePaymentRequest OfflinePaymentRequest) {

                User sender = userRepository.findById(senderId)
                                .orElseThrow(() -> new UserNotFoundException("Sender not Found " + senderId));

                User reciver = userRepository.findById(OfflinePaymentRequest.getReceiverId())
                                .orElseThrow(() -> new UserNotFoundException(
                                                "Reciver ID not found " + OfflinePaymentRequest.getReceiverId()));

                if (sender.getId() == reciver.getId()) {
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

                OfflineTransaction transaction = new OfflineTransaction();

                transaction.setTransactionReference("OFF- " + UUID.randomUUID());
                transaction.setSenderId(senderId);
                transaction.setReceiverId(reciver.getId());
                transaction.setAmount(amount);
                transaction.setStatus(OfflineTransactionStatus.PENDING);
                transaction.setCreatedAt(LocalDateTime.now());

                return offlineTransactionRepository.save(transaction);

        }

}
