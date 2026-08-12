package com.mayur.offline_UPI_system.services;

import org.springframework.stereotype.Service;
import java.util.List;
import com.mayur.offline_UPI_system.model.User;
import com.mayur.offline_UPI_system.repository.UserRepository;

@Service
public class UserServices {

    private final UserRepository userRepository;

    UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUsersById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(int id, User updatedUser) {
        User existingUser = userRepository.findById(id).orElse(null);

        if (existingUser != null) {
            existingUser.setName(updatedUser.getName());
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            existingUser.setUpiId(updatedUser.getUpiId());

            return userRepository.save(existingUser);
        }
        return null;

    }

    public boolean deleteUser(int id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
