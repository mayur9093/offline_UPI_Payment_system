package com.mayur.offline_UPI_system.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.mayur.offline_UPI_system.model.Transaction;
import com.mayur.offline_UPI_system.model.User;
import com.mayur.offline_UPI_system.model.TransactionStatus;
import com.mayur.offline_UPI_system.repository.TransactionRepository;

@Service
public class TransactionServices {

    private final TransactionRepository transactionRepository;

    public TransactionServices(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveFailedTransaction(
            User sender,
            User receiver,
            BigDecimal amount) {

        Transaction transaction = new Transaction();

        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setAmount(amount);
        transaction.setStatus(TransactionStatus.FAILED);
        transaction.setCreatedAt(LocalDateTime.now());

        transactionRepository.save(transaction);
    }
}