package com.mayur.offline_UPI_system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mayur.offline_UPI_system.model.OfflineTransection;

public interface OfflineTransactionRepository extends JpaRepository<OfflineTransection, Long> {
    Optional<OfflineTransection> findByTransactionReference(String transactionReference);
}
