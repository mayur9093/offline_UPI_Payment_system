package com.mayur.offline_UPI_system.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mayur.offline_UPI_system.repository.UserRepository;
import com.mayur.offline_UPI_system.repository.WalletRepositopry;
import com.mayur.offline_UPI_system.model.User;
import com.mayur.offline_UPI_system.model.Wallet;
import com.mayur.offline_UPI_system.dto.WalletRequest;

@Service
public class WalletServics {
    private final WalletRepositopry walletRepositopry;
    private final UserRepository userRepository;
    public WalletServics(WalletRepositopry walletRepositopry,UserRepository userRepository) {
        this.walletRepositopry = walletRepositopry;
        this.userRepository=userRepository;
    }

    public Wallet createWallet(WalletRequest request) {
        User user=userRepository.findById(request.getUserId())
        .orElseThrow( ()->
        new RuntimeException(
            "user not found id :" + request.getUserId()));
            Wallet wallet = new Wallet();

            wallet.setBalance(request.getBalance());
            wallet.setCurrency(request.getCurrency());

            wallet.setUser(user);

            return walletRepositopry.save(wallet);

    }

    public List<Wallet> getAllWallets() {
        return walletRepositopry.findAll();
    }

}
