package com.mayur.offline_UPI_system.controller;

import java.math.BigDecimal;

import com.mayur.offline_UPI_system.dto.WalletRequest;
import com.mayur.offline_UPI_system.model.Wallet;
import com.mayur.offline_UPI_system.services.WalletService;
import com.mayur.offline_UPI_system.dto.WalletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mayur.offline_UPI_system.dto.MoneyRequest;
import com.mayur.offline_UPI_system.model.User;
import org.springframework.security.core.Authentication;

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
    public Wallet getWMyallet(Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return walletService.getMyWallet(user.getId());
    }

    @PostMapping("/{userId}/deposit")
    public ResponseEntity<WalletResponse> deposit(@PathVariable int userId,
            @Valid @RequestBody MoneyRequest moneyRequest, Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        int loggedInUser = user.getId();

        WalletResponse response = walletService.deposit(userId, loggedInUser, moneyRequest);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/withdraw")
    public ResponseEntity<Wallet> withdraw(@PathVariable int userId, @Valid @RequestBody MoneyRequest moneyRequest,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        int loggedInUser = user.getId();

        Wallet wallet = walletService.withdraw(userId, loggedInUser, moneyRequest);

        return ResponseEntity.ok(wallet);

    }

    @GetMapping("/{userId}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable int userId, Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        int loggedInUser = user.getId();

        BigDecimal balance = walletService.getBalance(userId, loggedInUser);

        return ResponseEntity.ok(balance);

    }

}
