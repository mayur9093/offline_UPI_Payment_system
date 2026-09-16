package com.mayur.offline_UPI_system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mayur.offline_UPI_system.dto.OfflinePaymentRequest;
import com.mayur.offline_UPI_system.model.OfflineTransaction;
import com.mayur.offline_UPI_system.services.OfflinePaymentService;
import com.mayur.offline_UPI_system.model.User;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/Offline_Payments")
public class OfflinePaymentController {

    private final OfflinePaymentService offlinePaymentService;

    public OfflinePaymentController(OfflinePaymentService offlinePaymentService) {
        this.offlinePaymentService = offlinePaymentService;
    }

    @PostMapping("/create")
    public ResponseEntity<OfflineTransaction> createOfflinepayment(@Valid @RequestBody OfflinePaymentRequest request,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        OfflineTransaction transaction = offlinePaymentService.createOfflinepayment(user.getId(), request);

        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

}
