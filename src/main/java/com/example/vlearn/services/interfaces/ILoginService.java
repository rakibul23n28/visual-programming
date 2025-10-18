package com.example.vlearn.services.interfaces;

import com.example.vlearn.models.User;

import java.util.concurrent.CompletableFuture;

<<<<<<< HEAD

public interface ILoginService {
    CompletableFuture<User> getUserByUserNameAsync(String userName);

    // Add this method
    boolean isTokenValid(String token);
}
=======
public interface ILoginService {
    CompletableFuture<User> getUserByUserNameAsync(String userName);
}
;
>>>>>>> f1b259cc68276d4a9dd787fafa2d358bd9478c3e
