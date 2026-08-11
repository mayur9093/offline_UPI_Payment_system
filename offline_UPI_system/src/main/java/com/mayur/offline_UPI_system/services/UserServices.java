package com.mayur.offline_UPI_system.services;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import com.mayur.offline_UPI_system.model.User;

@Service
public class UserServices {

    private List<User> users = new ArrayList<>();

    public User createUser(@RequestBody User user) {
        users.add(user);
        return user;
    }

    public User getUsersById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public User updateUser(int id, User updatedUser) {
        for (User user : users) {
            if (user.getId() == id) {
                user.setName(updatedUser.getName());
                user.setPhoneNumber(updatedUser.getPhoneNumber());
                user.setUpiId(updatedUser.getUpiId());

                return user;
            }
        }
        return null;

    }

    public Boolean deleteUser(int id) {
        return users.removeIf(user -> user.getId() == id);
    }

    public List<User> getAllUsers() {
        return users;
    }

}
