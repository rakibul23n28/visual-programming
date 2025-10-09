package com.example.vlearn.services.interfaces.services;

import com.example.vlearn.db.DatabaseConnector;
import com.example.vlearn.models.User;
import com.example.vlearn.services.interfaces.ILoginService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class LoginService implements ILoginService {
    private final Connection connection;

    public LoginService() throws Exception {
        this.connection = DatabaseConnector.getConnection();
    }
    @Override
    public CompletableFuture<User> getUserByUserNameAsync(String userName) {
        return CompletableFuture.supplyAsync(() -> {
            String sql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setString(1, userName);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new User.Builder()
                            .username(rs.getString("userName"))
                            .firstName(rs.getString("firstName"))
                            .lastName(rs.getString("lastName"))
                            .password(rs.getString("password"))
                            .id(rs.getInt("id")).build();
                } else {
                    return null;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

