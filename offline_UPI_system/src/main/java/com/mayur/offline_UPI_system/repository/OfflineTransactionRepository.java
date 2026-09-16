package com.mayur.offline_UPI_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayur.offline_UPI_system.model.OfflineTransaction;

public interface OfflineTransactionRepository extends JpaRepository<OfflineTransaction, Long> {
    Optional<OfflineTransaction> findByTransactionReference(String transactionReference);
}
