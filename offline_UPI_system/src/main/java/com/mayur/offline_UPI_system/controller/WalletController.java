package com.mayur.offline_UPI_system.controller;

import java.util.List;

import com.mayur.offline_UPI_system.dto.WalletRequest;
import com.mayur.offline_UPI_system.model.Wallet;
import com.mayur.offline_UPI_system.services.WalletServics;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    private final WalletServics walletServics;

    public WalletController(WalletServics walletServics) {
        this.walletServics = walletServics;
    }

    @PostMapping
    public ResponseEntity<Wallet> createWallet(@Valid @RequestBody WalletRequest walletRequest) {
        Wallet wallet = walletServics.createWallet(walletRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(wallet);
    }

    @GetMapping
    public List<Wallet> getAllWallets() {
        return walletServics.getAllWallets();
    }
}
