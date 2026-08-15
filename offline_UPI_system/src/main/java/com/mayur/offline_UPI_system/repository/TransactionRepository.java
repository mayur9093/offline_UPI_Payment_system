package com.mayur.offline_UPI_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mayur.offline_UPI_system.model.Transaction;

public interface TransactionRepository  extends JpaRepository<Transaction, Integer>{
    
    
}
