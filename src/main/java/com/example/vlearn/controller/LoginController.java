package com.example.vlearn.controller;

import com.example.vlearn.services.LoginService;
import com.example.vlearn.services.interfaces.ILoginService;
import com.example.vlearn.util.CustomAlert;
import com.example.vlearn.util.Hasher;
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
import java.util.concurrent.CompletableFuture;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    public ImageView backgroundImage;
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
        // Load background image (placeholder URL, replace with actual image path or URL)
        Image image = new Image("https://images.unsplash.com/photo-1600585154340-be6161a56a0c");
        backgroundImage.setImage(image);
        backgroundImage.setOpacity(0.3);
    }

    @FXML
    private void handleLogin() throws IOException {
        isValidUser().thenAccept(valid -> {
            if (valid) {
                Platform.runLater(() -> {
                    try {
                        System.out.println("ksjdkjshdkjshd");
                        loadDashboardForm();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                System.out.println("Login failed attempted with username: " + usernameField.getText());

            }
        });
    }

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
                        System.out.println(hashPassword);
                        System.out.println(user);
                        System.out.println(user.getFirstName());
                        System.out.println(user.getPassword());

                        if (Objects.equals(user.getPassword(), hashPassword)) {
                            return true;
                        } else {
                            Platform.runLater(() ->
                                    CustomAlert.showAlert("Login", "Invalid user.", Alert.AlertType.ERROR));

                        }
                    } else {
                        Platform.runLater(() ->
                                CustomAlert.showAlert("Login", "Invalid user.", Alert.AlertType.ERROR));
                    }
                    return false;
                });
    }
}