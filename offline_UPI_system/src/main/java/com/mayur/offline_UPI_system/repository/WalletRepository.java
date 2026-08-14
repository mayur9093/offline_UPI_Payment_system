package com.mayur.offline_UPI_system.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.mayur.offline_UPI_system.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    Optional<Wallet> findByUserId(int userId);

}
