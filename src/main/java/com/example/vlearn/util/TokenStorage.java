package com.example.vlearn.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TokenStorage {
    private static final String TOKEN_FILE = "user_authenticate.txt";

    public static void saveToken(String token) {
        try {
            Files.writeString(Path.of(TOKEN_FILE), token);
            System.out.println("Token saved to " + TOKEN_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadToken() {
        try {
            if (Files.exists(Path.of(TOKEN_FILE))) {
                return Files.readString(Path.of(TOKEN_FILE));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void clearToken() {
        try {
            Files.deleteIfExists(Path.of(TOKEN_FILE));
            System.out.println("Token cleared");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
