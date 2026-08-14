package com.mayur.offline_UPI_system.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.mayur.offline_UPI_system.dto.TransferRequest;
import com.mayur.offline_UPI_system.model.User;
import com.mayur.offline_UPI_system.model.Wallet;
import com.mayur.offline_UPI_system.repository.*;

@Service
public class PaymentService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;

    public PaymentService(UserRepository userRepository, WalletRepository walletRepository) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
    }

    public String transfer(TransferRequest transferRequest) {
            User sender = userRepository.findById(transferRequest.getSenderId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Sender not found: "
                                + transferRequest.getSenderId()
                        ));

        User receiver = userRepository.findById(transferRequest.getReceiverId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Receiver not found: "
                                + transferRequest.getReceiverId()
                        ));

        Wallet senderWallet =
                walletRepository.findByUserId(sender.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Sender wallet not found"
                                ));

         Wallet receiverWallet =
                walletRepository.findByUserId(receiver.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Receiver wallet not found"
                                ));


        BigDecimal amount = transferRequest.getAmount();

        if (senderWallet.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        senderWallet.setBalance(
                senderWallet.getBalance().subtract(amount)
        );

        receiverWallet.setBalance(
                receiverWallet.getBalance().add(amount)
        );

        walletRepository.save(senderWallet);
        walletRepository.save(receiverWallet);

        return "Transfer successful";
    }
}
