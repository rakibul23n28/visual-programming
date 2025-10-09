package com.example.vlearn.services;

import com.example.vlearn.db.DatabaseConnector;
import com.example.vlearn.models.User;
import com.example.vlearn.services.interfaces.IUserService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;

public class UserService implements IUserService {
    private final Connection connection;

    public UserService() throws Exception {
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
                            .id(rs.getInt("id")).build();
                } else {
                    return null;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<User> saveUserAsync(User user) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String checkUserSql = "SELECT id FROM users WHERE username = ?";
                try (PreparedStatement checkStmt = connection.prepareStatement(checkUserSql)) {
                    checkStmt.setString(1, user.getUsername());
                    ResultSet rs = checkStmt.executeQuery();

                    if (rs.next()) {
                        // Update existing user
                        return updateUser(user, rs.getInt("id"));
                    } else {
                        // Insert new user
                        return insertUser(user);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error during insertOrUpdateUser", e);
            }
        });
    }

    private User insertUser(User user) throws SQLException {
        String insertSql = "INSERT INTO users (username, password, firstName, lastName) " +
                "VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
            insertStmt.setString(1, user.getUsername());
            insertStmt.setString(2, user.getPassword());
            insertStmt.setString(3, user.getFirstName());
            insertStmt.setString(4, user.getLastName());
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
        return user;
    }

    private User updateUser(User user, int id) throws SQLException {
        String updateSql = "UPDATE users SET password = ?, firstName = ?, " +
                "lastName = ? WHERE id = ?";
        try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
            updateStmt.setString(1, user.getPassword());
            updateStmt.setString(2, user.getFirstName());
            updateStmt.setString(3, user.getLastName());
            updateStmt.setInt(4, id);
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
        return user;
    }

    private void deleteUser(int id) throws SQLException {
        String updateSql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
            updateStmt.setInt(1, id);
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }
}
