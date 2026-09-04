package com.mayur.offline_UPI_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.core.Authentication;
import com.mayur.offline_UPI_system.dto.TransactionResponse;
import com.mayur.offline_UPI_system.dto.TransferRequest;
import com.mayur.offline_UPI_system.model.User;
import com.mayur.offline_UPI_system.services.PaymentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(@Valid @RequestBody TransferRequest transferRequest,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        int senderId = user.getId();

        return ResponseEntity.ok(paymentService.transfer(senderId, transferRequest));
    }

    @GetMapping("/history")
    public ResponseEntity<List<TransactionResponse>> getTransactionHistory(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        int userId = user.getId();

        List<TransactionResponse> history = paymentService.getTransactionHistory(userId);

        return ResponseEntity.ok(history);

    }

}
