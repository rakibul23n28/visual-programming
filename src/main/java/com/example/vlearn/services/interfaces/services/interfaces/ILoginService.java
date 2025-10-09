package com.example.vlearn.services.interfaces.services.interfaces;

import com.example.vlearn.models.User;

import java.util.concurrent.CompletableFuture;

public interface ILoginService {
    CompletableFuture<User> getUserByUserNameAsync(String userName);
}
;