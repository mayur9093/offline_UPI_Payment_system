package com.mayur.offline_UPI_system.controller;

import java.math.BigDecimal;
import java.util.List;

import com.mayur.offline_UPI_system.dto.WalletRequest;
import com.mayur.offline_UPI_system.model.Wallet;
import com.mayur.offline_UPI_system.services.WalletService;
import com.mayur.offline_UPI_system.dto.WalletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mayur.offline_UPI_system.dto.MoneyRequest;

@RestController
@RequestMapping("/wallets")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<Wallet> createWallet(@Valid @RequestBody WalletRequest walletRequest) {
        Wallet wallet = walletService.createWallet(walletRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(wallet);
    }

    @GetMapping
    public List<Wallet> getAllWallets() {
        return walletService.getAllWallets();
    }

    @PostMapping("/{userId}/deposit")
    public ResponseEntity<WalletResponse> deposit(@PathVariable int userId, @Valid @RequestBody MoneyRequest moneyRequest) {

        WalletResponse response = walletService.deposit(userId, moneyRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/withdraw")
    public ResponseEntity<Wallet> withdraw(@PathVariable int userId, @Valid @RequestBody MoneyRequest moneyRequest) {

        Wallet wallet = walletService.withdraw(userId, moneyRequest);

        return ResponseEntity.ok(wallet);

    }

    @GetMapping("/{userId}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable int userId) {

        BigDecimal balance = walletService.getBalance(userId);

        return ResponseEntity.ok(balance);
    }



}
