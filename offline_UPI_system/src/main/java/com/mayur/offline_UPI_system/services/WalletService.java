package com.mayur.offline_UPI_system.services;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import com.mayur.offline_UPI_system.repository.UserRepository;
import com.mayur.offline_UPI_system.repository.WalletRepository;
import com.mayur.offline_UPI_system.exception.UserNotFoundException;
import com.mayur.offline_UPI_system.exception.WalletNotFoundException;
import com.mayur.offline_UPI_system.model.User;
import com.mayur.offline_UPI_system.model.Wallet;
//import com.mayur.offline_UPI_system.dto.WalletRequest;
import com.mayur.offline_UPI_system.dto.MoneyRequest;
import com.mayur.offline_UPI_system.dto.WalletResponse;
import com.mayur.offline_UPI_system.exception.InsufficientBalanceException;
import com.mayur.offline_UPI_system.exception.AccessDeniedException;

@Service
public class WalletService {
    private final WalletRepository walletRepository;

    private final UserRepository userRepository;

    public WalletService(WalletRepository walletRepository, UserRepository userRepository) {
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
    }

    // public Wallet createWallet(WalletRequest request) {
    // User user = userRepository.findById(request.getUserId())
    // .orElseThrow(() -> new UserNotFoundException("user not found id :" +
    // request.getUserId()));
    // Wallet wallet = new Wallet();
    //
    // wallet.setBalance(request.getBalance());
    // wallet.setCurrency(request.getCurrency());
    //
    // wallet.setUser(user);
    //
    // return walletRepository.save(wallet);
    //
    // }

    public Wallet getMyWallet(int userId) {

        return walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException(
                        "Wallet not found for user: " + userId));
    }

    public WalletResponse deposit(int userId, int loggedInUser, MoneyRequest moneyRequest) {
        if (userId != loggedInUser) {
            throw new AccessDeniedException("You are not allowed to access this wallet");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found id : " + userId));

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found for User : " + userId));

        BigDecimal currentBalance = wallet.getBalance();
        if (currentBalance == null) {
            currentBalance = BigDecimal.ZERO;
        }
        BigDecimal newBalance = currentBalance.add(moneyRequest.getAmount());

        wallet.setBalance(newBalance);

        Wallet savedWallet = walletRepository.save(wallet);
        return new WalletResponse(savedWallet.getId(), user.getId(), savedWallet.getBalance(),
                savedWallet.getCurrency());
    }

    public Wallet withdraw(int userId, int loggedInUser, MoneyRequest moneyRequest) {

        if (userId != loggedInUser) {
            throw new AccessDeniedException("You are not allowed to access this wallet");
        }

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found id : " + userId));

        BigDecimal currentBalance = wallet.getBalance();

        BigDecimal amount = moneyRequest.getAmount();

        if (currentBalance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("insufficient Balance");
        }

        BigDecimal newBalance = currentBalance.subtract(amount);

        wallet.setBalance(newBalance);

        return walletRepository.save(wallet);
    }

    public BigDecimal getBalance(int userId, int loggedInUser) {

        if (userId != loggedInUser) {
            throw new AccessDeniedException("You are not allowed to access this wallet");
        }

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet user not found id : " + userId));

        return wallet.getBalance();
    }
}
