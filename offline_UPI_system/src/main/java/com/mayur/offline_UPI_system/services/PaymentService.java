package com.mayur.offline_UPI_system.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mayur.offline_UPI_system.dto.TransactionResponse;
import com.mayur.offline_UPI_system.dto.TransferRequest;
import com.mayur.offline_UPI_system.exception.InsufficientBalanceException;
import com.mayur.offline_UPI_system.exception.InvalidAmountException;
import com.mayur.offline_UPI_system.exception.UserNotFoundException;
import com.mayur.offline_UPI_system.exception.WalletNotFoundException;
import com.mayur.offline_UPI_system.model.User;
import com.mayur.offline_UPI_system.model.Wallet;
import com.mayur.offline_UPI_system.repository.UserRepository;
import com.mayur.offline_UPI_system.repository.WalletRepository;

import com.mayur.offline_UPI_system.model.Transaction;
import com.mayur.offline_UPI_system.model.TransactionStatus;
import com.mayur.offline_UPI_system.repository.TransactionRepository;

@Service
public class PaymentService {

        private final UserRepository userRepository;
        private final WalletRepository walletRepository;
        private final TransactionRepository transactionRepository;

        public PaymentService(UserRepository userRepository, WalletRepository walletRepository,
                        TransactionRepository transactionRepository) {
                this.userRepository = userRepository;
                this.walletRepository = walletRepository;
                this.transactionRepository = transactionRepository;
        }

        @Transactional
        public String transfer(TransferRequest transferRequest) {
                User sender = userRepository.findById(transferRequest.getSenderId())
                                .orElseThrow(() -> new UserNotFoundException(
                                                "Sender not found: "
                                                                + transferRequest.getSenderId()));

                User receiver = userRepository.findById(transferRequest.getReceiverId())
                                .orElseThrow(() -> new UserNotFoundException(
                                                "Receiver not found: "
                                                                + transferRequest.getReceiverId()));

                Wallet senderWallet = walletRepository.findByUserId(sender.getId())
                                .orElseThrow(() -> new WalletNotFoundException(
                                                "Sender wallet not found for user id: " + sender.getId()));

                Wallet receiverWallet = walletRepository.findByUserId(receiver.getId())
                                .orElseThrow(() -> new WalletNotFoundException(
                                                "Receiver wallet not found for user id: " + receiver.getId()));

                BigDecimal amount = transferRequest.getAmount();

                if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
                        throw new InvalidAmountException(
                                        "Transfer amount must be greater than zero");
                }

                if (senderWallet.getBalance().compareTo(amount) < 0) {
                        throw new InsufficientBalanceException("Insufficient balance. Sender has "
                                        + senderWallet.getBalance() + ", tried to send " + amount);
                }

                senderWallet.setBalance(
                                senderWallet.getBalance().subtract(amount));

                receiverWallet.setBalance(
                                receiverWallet.getBalance().add(amount));

                walletRepository.save(senderWallet);
                walletRepository.save(receiverWallet);

                Transaction transaction = new Transaction();

                transaction.setSender(sender);
                transaction.setReceiver(receiver);
                transaction.setAmount(amount);
                transaction.setStatus(TransactionStatus.SUCCESS);
                transaction.setCreatedAt(LocalDateTime.now());

                transactionRepository.save(transaction);

                return "Transfer successful";
        }

        public List<TransactionResponse> getTransactionHistory(int userId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

                List<Transaction> transactions = transactionRepository.findBySenderOrReceiver(user, user);

                return transactions.stream()
                                .map(transaction -> {

                                        TransactionResponse response = new TransactionResponse();

                                        response.setTransactionId(
                                                        transaction.getId());

                                        response.setSenderId(
                                                        transaction.getSender() != null
                                                                        ? transaction.getSender().getId()
                                                                        : 0);

                                        response.setReceiverId(
                                                        transaction.getReceiver() != null
                                                                        ? transaction.getReceiver().getId()
                                                                        : 0);

                                        response.setAmount(
                                                        transaction.getAmount());

                                        response.setStatus(
                                                        transaction.getStatus());

                                        response.setCreatedAt(
                                                        transaction.getCreatedAt());

                                        return response;
                                })
                                .toList();
        }
}
