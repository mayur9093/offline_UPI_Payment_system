package com.mayur.offline_UPI_system.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.mayur.offline_UPI_system.model.User;
import java.util.List;
import com.mayur.offline_UPI_system.services.UserService;

import jakarta.validation.Valid;

import com.mayur.offline_UPI_system.dto.UserRequest;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public User createUser(
            @Valid @RequestBody UserRequest request) {
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        return userService.getUsersById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(
            @PathVariable int id,
            @RequestBody User updatedUser) {

        return userService.updateUser(id, updatedUser);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
