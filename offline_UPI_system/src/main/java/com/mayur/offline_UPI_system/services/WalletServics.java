package com.mayur.offline_UPI_system.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mayur.offline_UPI_system.repository.WalletRepositopry;
import com.mayur.offline_UPI_system.model.Wallet;
import com.mayur.offline_UPI_system.dto.WalletRequest;

@Service
public class WalletServics {
    private final WalletRepositopry walletRepositopry;

    public WalletServics(WalletRepositopry walletRepositopry) {
        this.walletRepositopry = walletRepositopry;
    }

    public Wallet createWallet(WalletRequest request) {
        Wallet wallet = new Wallet();

        wallet.setBalance(request.getBalance());
        wallet.setCurrency(request.getCurrency());

        return walletRepositopry.save(wallet);

    }

    public List<Wallet> getAllWallets() {
        return walletRepositopry.findAll();
    }

}
