package com.mayur.offline_UPI_system.controller;

import org.springframework.web.bind.annotation.*;
import com.mayur.offline_UPI_system.model.User;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    List<User> users = new ArrayList<>();

    @GetMapping
    public List<User> getUsers() {
        return users;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        users.add(user);
        return user;
    }

    @GetMapping("{id}")
    public User getUser(@PathVariable int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }

        return null;
    }

    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable int id,
            @RequestBody User updatedUser) {

        for (User user : users) {

            if (user.getId() == id) {

                user.setName(updatedUser.getName());
                user.setUpiId(updatedUser.getUpiId());
                user.setPhoneNumber(updatedUser.getPhoneNumber());

                return user;
            }
        }

        return null;
    }

    @DeleteMapping("{id}")
    public String deleteUser(@PathVariable int id) {
        Boolean removed = users.removeIf(user -> user.getId() == id);

        if (removed) {
            return "user Removed";
        }
        return "user not found";
    }
}
