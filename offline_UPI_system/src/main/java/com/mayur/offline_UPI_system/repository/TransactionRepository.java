package com.mayur.offline_UPI_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mayur.offline_UPI_system.model.Transaction;
import com.mayur.offline_UPI_system.model.User;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    List<Transaction> findBySenderOrReceiver(
        User sender,
        User receiver
    );

}
