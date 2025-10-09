package com.example.vlearn.services.interfaces;

import com.example.vlearn.models.User;

import java.util.concurrent.CompletableFuture;

public interface IUserService {
    CompletableFuture<User> getUserByUserNameAsync(String username);
    CompletableFuture<User> saveUserAsync(User user);
}
