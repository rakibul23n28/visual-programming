package com.example.vlearn.services.interfaces;

import com.example.vlearn.models.User;

import java.util.concurrent.CompletableFuture;


public interface ILoginService {
    CompletableFuture<User> getUserByUserNameAsync(String userName);

    // Add this method
    boolean isTokenValid(String token);
}
