package com.mayur.offline_UPI_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mayur.offline_UPI_system.model.Wallet;

public interface WalletRepositopry extends JpaRepository<Wallet, Integer> {

}
