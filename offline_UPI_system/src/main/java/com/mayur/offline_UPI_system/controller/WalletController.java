package com.mayur.offline_UPI_system.controller;

import java.math.BigDecimal;
import java.util.List;

import com.mayur.offline_UPI_system.dto.WalletRequest;
import com.mayur.offline_UPI_system.model.Wallet;
import com.mayur.offline_UPI_system.services.WalletServics;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mayur.offline_UPI_system.dto.MoneyRequest;



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

    @PostMapping("/{userId}/deposit")
    public ResponseEntity<Wallet> deposit(@PathVariable int userId , @Valid @RequestBody MoneyRequest moneyRequest){

        Wallet wallet = walletServics.deposit(userId, moneyRequest);

        return  ResponseEntity.ok(wallet);
    }

    @GetMapping("/{userId}/withdraw")
    public ResponseEntity<Wallet> withdraw( @PathVariable int userId,@Valid @RequestBody MoneyRequest moneyRequest) {

        Wallet wallet = walletServics.withdraw(userId, moneyRequest);

        return ResponseEntity.ok(wallet);
        
    }

    @GetMapping("/{userId}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable int userId) {

        BigDecimal balance = walletServics.getBalance(userId);

        return ResponseEntity.ok(balance);
    }
    

    
}
