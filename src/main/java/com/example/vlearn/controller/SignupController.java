package com.example.vlearn.controller;

import com.example.vlearn.models.User;
import com.example.vlearn.services.UserService;
import com.example.vlearn.services.interfaces.IUserService;
import com.example.vlearn.util.CustomAlert;
import com.example.vlearn.util.Hasher;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class SignupController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField retypePasswordField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button submitBtn;
    @FXML
    public ImageView backgroundImage;
    @FXML
    private Label messageLabel;

    IUserService userService;
    private Scene sceneLogin;
    private Stage primaryStage;

    public void setSceneLogin(Scene loginScene) {
        this.sceneLogin = loginScene;
    }

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    @FXML
    public void initialize() throws Exception {
        userService = new UserService();


        submitBtn.setOnAction(e -> {
            try {
                handleSubmit();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    @FXML
    private void cancelSignUp() {
        if (primaryStage != null && sceneLogin != null) {
            primaryStage.setScene(sceneLogin);
            primaryStage.setMaximized(true);
        }
    }

    @FXML
    private void handleSubmit() throws Exception {
        validationAsync().thenAccept(valid -> {
            if (valid) {
                Platform.runLater(() -> {
                    String hashPassword = Hasher.hash(passwordField.getText().toCharArray(), usernameField.getText());
                    User user = new User.Builder()
                            .username(usernameField.getText())
                            .password(hashPassword)
                            .firstName(firstNameField.getText())
                            .lastName(lastNameField.getText())
                            .build();

                    userService.saveUserAsync(user)
                            .thenAccept(savedUser -> Platform.runLater(() -> {
                                CustomAlert.showAlert("User Service", "User saved: " + savedUser.getUsername(), Alert.AlertType.INFORMATION);
                                if (primaryStage != null && sceneLogin != null) {
                                    primaryStage.setScene(sceneLogin);
                                }
                            }))
                            .exceptionally(ex -> {
                                Platform.runLater(() -> CustomAlert.showAlert("User Service", "Failed to save user: " + ex.getLocalizedMessage(), Alert.AlertType.ERROR));
                                return null;
                            });
                });
            }
        });
    }

    public CompletableFuture<Boolean> validationAsync() {
        // Synchronous validations
        if (firstNameField.getText().isEmpty()) {
            CustomAlert.showAlert("Signup", "Please enter first name", Alert.AlertType.INFORMATION);
            return CompletableFuture.completedFuture(false);
        }
        if (lastNameField.getText().isEmpty()) {
            CustomAlert.showAlert("Signup", "Please enter last name", Alert.AlertType.INFORMATION);
            return CompletableFuture.completedFuture(false);
        }
        if (usernameField.getText().isEmpty()) {
            CustomAlert.showAlert("Signup", "Please enter user name", Alert.AlertType.INFORMATION);
            return CompletableFuture.completedFuture(false);
        }
        if (passwordField.getText().isEmpty()) {
            CustomAlert.showAlert("Signup", "Please enter password", Alert.AlertType.INFORMATION);
            return CompletableFuture.completedFuture(false);
        }
        if (retypePasswordField.getText().isEmpty()) {
            CustomAlert.showAlert("Signup", "Please retype the password", Alert.AlertType.INFORMATION);
            return CompletableFuture.completedFuture(false);
        }
        if (!Objects.equals(passwordField.getText(), retypePasswordField.getText())) {
            CustomAlert.showAlert("Signup", "Password and retype password do not match.", Alert.AlertType.INFORMATION);
            return CompletableFuture.completedFuture(false);
        }

        String userName = usernameField.getText().trim();

        return userService.getUserByUserNameAsync(userName)
                .handle((user, ex) -> {
                    if (ex != null) {
                        Platform.runLater(() ->
                                CustomAlert.showAlert("Signup", "Error checking user: " + ex.getCause().getMessage(), Alert.AlertType.ERROR));
                        return false;
                    }
                    if (user != null) {
                        Platform.runLater(() ->
                                CustomAlert.showAlert("Signup", "Username already exists.", Alert.AlertType.ERROR));
                        return false;
                    }
                    return true;
                });
    }
}
