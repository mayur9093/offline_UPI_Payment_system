package com.mayur.offline_UPI_system.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

import com.mayur.offline_UPI_system.exception.InvalidCredentialsException;
import com.mayur.offline_UPI_system.exception.UserNotFoundException;
import com.mayur.offline_UPI_system.model.User;
import com.mayur.offline_UPI_system.repository.UserRepository;
import com.mayur.offline_UPI_system.dto.LoginRequest;
import com.mayur.offline_UPI_system.dto.LoginResponse;
import com.mayur.offline_UPI_system.dto.UserRegisterRequest;
import com.mayur.offline_UPI_system.dto.UserRequest;
import com.mayur.offline_UPI_system.services.UserService;

import java.math.BigDecimal;

import com.mayur.offline_UPI_system.model.Wallet;
import com.mayur.offline_UPI_system.repository.WalletRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WalletRepository walletRepository;
    private final JwtService jwtService;

    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, WalletRepository walletRepository,
            JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.walletRepository = walletRepository;
        this.jwtService = jwtService;
    }

    public User createUser(UserRequest request) {
        User user = new User();

        user.setName(request.getName());
        user.setUpiId(request.getUpiId());
        user.setPhoneNumber(request.getPhoneNumber());

        return userRepository.save(user);
    }

    public User getUsersById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user not found id : " + id));
    }

    public User updateUser(int id, User updatedUser) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("user not found : " + id));

        existingUser.setName(updatedUser.getName());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setUpiId(updatedUser.getUpiId());

        return userRepository.save(existingUser);

    }

    public void deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("user not found id : " + id);
        }
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User registUser(UserRegisterRequest userRegisterRequest) {

        User user = new User();

        user.setName(userRegisterRequest.getName());
        user.setUpiId(userRegisterRequest.getUpiId());
        user.setPhoneNumber(userRegisterRequest.getPhoneNumber());

        String hashPassword = passwordEncoder.encode(userRegisterRequest.getPassword());

        user.setPassword(hashPassword);

        User savedUser = userRepository.save(user);

        Wallet wallet = new Wallet();

        wallet.setBalance(BigDecimal.ZERO);
        wallet.setCurrency("INR");
        wallet.setUser(savedUser);

        walletRepository.save(wallet);

        return savedUser;

    }

    public LoginResponse login(LoginRequest loginRequest) {

        User user = userRepository.findByUpiId(loginRequest.getUpiId())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid UPI ID or password"));

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {

            throw new InvalidCredentialsException("Invalid UPI ID or password");

        }

        String token = jwtService.generateToken(user.getId());

        return new LoginResponse(user.getId(), token);
    }

}
