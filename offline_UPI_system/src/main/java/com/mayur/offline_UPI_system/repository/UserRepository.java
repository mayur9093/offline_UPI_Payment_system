package com.mayur.offline_UPI_system.repository;

import com.mayur.offline_UPI_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUpiId(String upiId);
}
