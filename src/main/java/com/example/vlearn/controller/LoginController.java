package com.example.vlearn.controller;

import com.example.vlearn.services.LoginService;
import com.example.vlearn.services.interfaces.ILoginService;
import com.example.vlearn.util.CustomAlert;
import com.example.vlearn.util.Hasher;
<<<<<<< HEAD
import com.example.vlearn.util.TokenStorage;
=======
>>>>>>> f1b259cc68276d4a9dd787fafa2d358bd9478c3e
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
<<<<<<< HEAD
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class LoginController {

=======
import java.util.concurrent.CompletableFuture;

public class LoginController {
>>>>>>> f1b259cc68276d4a9dd787fafa2d358bd9478c3e
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    public ImageView backgroundImage;
<<<<<<< HEAD

=======
>>>>>>> f1b259cc68276d4a9dd787fafa2d358bd9478c3e
    ILoginService loginService;
    private Scene signupScene;
    private Scene dashboardScene;
    private Stage primaryStage;

    public void setSignUpScene(Scene signupScene) {
        this.signupScene = signupScene;
    }

    public void setDashboardScene(Scene dashboardScene) {
        this.dashboardScene = dashboardScene;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    private void initialize() throws Exception {
        loginService = new LoginService();
<<<<<<< HEAD


        // Auto-login if token exists
        String token = TokenStorage.loadToken();
        if (token != null && loginService.isTokenValid(token)) {
            Platform.runLater(() -> {
                try {
                    loadDashboardForm();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @FXML
    private void handleLogin() {
=======
        // Load background image (placeholder URL, replace with actual image path or URL)
        Image image = new Image("https://images.unsplash.com/photo-1600585154340-be6161a56a0c");
        backgroundImage.setImage(image);
        backgroundImage.setOpacity(0.3);
    }

    @FXML
    private void handleLogin() throws IOException {
>>>>>>> f1b259cc68276d4a9dd787fafa2d358bd9478c3e
        isValidUser().thenAccept(valid -> {
            if (valid) {
                Platform.runLater(() -> {
                    try {
<<<<<<< HEAD
                        // Switch to dashboard using primaryStage
                        if (primaryStage != null && dashboardScene != null) {
                            primaryStage.setScene(dashboardScene);
                            primaryStage.setTitle("Dashboard");
                            primaryStage.setMaximized(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } else {
                Platform.runLater(() ->
                        CustomAlert.showAlert("Login", "Invalid username or password.", Alert.AlertType.ERROR)
                );
=======
                        System.out.println("ksjdkjshdkjshd");
                        loadDashboardForm();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                System.out.println("Login failed attempted with username: " + usernameField.getText());

>>>>>>> f1b259cc68276d4a9dd787fafa2d358bd9478c3e
            }
        });
    }

<<<<<<< HEAD

=======
>>>>>>> f1b259cc68276d4a9dd787fafa2d358bd9478c3e
    @FXML
    private void handleSignUp() throws IOException {
        loadSignupForm();
    }

    private void loadSignupForm() throws IOException {
        if (primaryStage != null && signupScene != null) {
            primaryStage.setTitle("Signup");
            primaryStage.setScene(signupScene);
        }
    }

    private void loadDashboardForm() throws IOException {
        if (primaryStage != null && dashboardScene != null) {
            primaryStage.setTitle("Dashboard");
            primaryStage.setScene(dashboardScene);
            primaryStage.setMaximized(true);
        }
    }

    private CompletableFuture<Boolean> isValidUser() {
        return loginService.getUserByUserNameAsync(usernameField.getText())
                .handle((user, ex) -> {
                    if (ex != null) {
                        Platform.runLater(() ->
                                CustomAlert.showAlert("Login", "Error checking user: " + ex.getCause().getMessage(), Alert.AlertType.ERROR));
                        return false;
                    }
                    if (user != null) {
                        String hashPassword = Hasher.hash(passwordField.getText().toCharArray(), usernameField.getText());
<<<<<<< HEAD
                        if (Objects.equals(user.getPassword(), hashPassword)) {
                            // Save token
                            String token = generateTokenForUser(user);
                            TokenStorage.saveToken(token);
=======
                        System.out.println(hashPassword);
                        System.out.println(user);
                        System.out.println(user.getFirstName());
                        System.out.println(user.getPassword());

                        if (Objects.equals(user.getPassword(), hashPassword)) {
>>>>>>> f1b259cc68276d4a9dd787fafa2d358bd9478c3e
                            return true;
                        } else {
                            Platform.runLater(() ->
                                    CustomAlert.showAlert("Login", "Invalid user.", Alert.AlertType.ERROR));
<<<<<<< HEAD
=======

>>>>>>> f1b259cc68276d4a9dd787fafa2d358bd9478c3e
                        }
                    } else {
                        Platform.runLater(() ->
                                CustomAlert.showAlert("Login", "Invalid user.", Alert.AlertType.ERROR));
                    }
                    return false;
                });
    }
<<<<<<< HEAD

    private String generateTokenForUser(Object user) {
        // You can improve this with a backend token
        return UUID.randomUUID().toString();
    }
}
=======
}
>>>>>>> f1b259cc68276d4a9dd787fafa2d358bd9478c3e
