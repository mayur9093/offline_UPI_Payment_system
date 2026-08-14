package com.mayur.offline_UPI_system.services;

import java.util.List;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

import com.mayur.offline_UPI_system.repository.UserRepository;
import com.mayur.offline_UPI_system.repository.WalletRepositopry;
import com.mayur.offline_UPI_system.model.User;
import com.mayur.offline_UPI_system.model.Wallet;
import com.mayur.offline_UPI_system.dto.WalletRequest;
import com.mayur.offline_UPI_system.dto.WalletResponse;
import com.mayur.offline_UPI_system.dto.MoneyRequest;
import com.mayur.offline_UPI_system.exception.InsufficientBalanceException;

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
    
    public WalletResponse deposit(int userId,MoneyRequest moneyRequest){

        Wallet wallet = walletRepositopry.findByUserId(userId).orElseThrow(()->
            new RuntimeException("Wallet not found id : "+userId));

            BigDecimal currentBalance = wallet.getBalance();

            BigDecimal amount = moneyRequest.getAmount();

            BigDecimal newBalance = currentBalance.add(amount);

            wallet.setBalance(newBalance);

            Wallet savedWallet = walletRepositopry.save(wallet);

            return new WalletResponse(savedWallet.getId(), savedWallet.getUser().getId(), savedWallet.getBalance(), savedWallet.getCurrency());
    }

    public Wallet withdraw(int userId , MoneyRequest moneyRequest){

        Wallet wallet = walletRepositopry.findByUserId(userId).orElseThrow(() ->
            new RuntimeException("Wallet not found id : "+ userId));

        BigDecimal currentBalance = wallet.getBalance();
        
        BigDecimal amount = moneyRequest.getAmount();

        if(currentBalance.compareTo(amount)<0){
            throw new InsufficientBalanceException("insufficient Balance");
        }

        BigDecimal newBigDecimal = currentBalance.subtract(amount);

        wallet.setBalance(newBigDecimal);

        return walletRepositopry.save(wallet);
    }

    public BigDecimal getBalance(int userId){

        Wallet wallet = walletRepositopry.findByUserId(userId).orElseThrow(() ->
        new RuntimeException("Wallet user not found id : "+userId));


        return wallet.getBalance();
    }

    private WalletResponse toWalletResponse(Wallet wallet){
        return new WalletResponse(wallet.getId(), wallet.getUser().getId(), wallet.getBalance(), wallet.getCurrency());
    }
}
