package com.mayur.offline_UPI_system.repository;

import com.mayur.offline_UPI_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
