package com.mayur.offline_UPI_system.services;

import org.springframework.stereotype.Service;
import java.util.List;

import com.mayur.offline_UPI_system.exception.UserNotFoundException;
import com.mayur.offline_UPI_system.model.User;
import com.mayur.offline_UPI_system.repository.UserRepository;
import com.mayur.offline_UPI_system.dto.UserRequest;

@Service
public class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

}
