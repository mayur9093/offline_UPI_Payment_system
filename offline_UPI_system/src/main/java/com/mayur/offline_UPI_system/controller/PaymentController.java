package com.mayur.offline_UPI_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mayur.offline_UPI_system.dto.TransferRequest;
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
    public ResponseEntity<String> transfer(@Valid @RequestBody TransferRequest transferRequest) {

        String result = paymentService.transfer(transferRequest);
        
        return ResponseEntity.ok(result);
    }


}
