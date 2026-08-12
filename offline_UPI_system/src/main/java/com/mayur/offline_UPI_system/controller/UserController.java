package com.mayur.offline_UPI_system.controller;

import org.springframework.web.bind.annotation.*;
import com.mayur.offline_UPI_system.model.User;
import java.util.List;
import com.mayur.offline_UPI_system.services.UserServices;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServices UserServices;

    public UserController(UserServices userServices) {
        this.UserServices = userServices;
    }

    @GetMapping
    public List<User> getUsers() {
        return UserServices.getAllUsers();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return UserServices.createUser(user);
    }

    @GetMapping("{id}")
    public User getUserById(@PathVariable int id) {
        return UserServices.getUsersById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable int id,
            @RequestBody User updatedUser) {

        return UserServices.updateUser(id, updatedUser);

    }

    @DeleteMapping("{id}")
    public String deleteUser(@PathVariable int id) {
        Boolean removed = UserServices.deleteUser(id);

        if (removed) {
            return "user Removed";
        }
        return "user not found";
    }
}
