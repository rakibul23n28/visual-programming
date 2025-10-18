package com.example.vlearn;

import com.example.vlearn.controller.HelloController;
import com.example.vlearn.controller.LoginController;
import com.example.vlearn.controller.SignupController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    private Scene loginScene;
    private Scene signupScene;
    private Scene dashboardScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Prepare all scenes and controllers
            LoginController loginController = prepareLoginScene();
            SignupController signupController = prepareSignUpScene();
            HelloController dashboardController = prepareDashboard();

            // Set stage and scenes for HelloController
            dashboardController.setPrimaryStage(primaryStage);
            dashboardController.setLoginScene(loginScene);
            dashboardController.setDashboardScene(dashboardScene);

            // Set stage and scenes for LoginController
            loginController.setPrimaryStage(primaryStage);
            loginController.setSignUpScene(signupScene);
            loginController.setDashboardScene(dashboardScene);

            // Set stage and scene for SignupController
            signupController.setPrimaryStage(primaryStage);
            signupController.setSceneLogin(loginScene);

            primaryStage.setTitle("Login");
            primaryStage.setScene(loginScene);
            primaryStage.show();

            // Make the window responsive
            primaryStage.setMinWidth(400);
            primaryStage.setMinHeight(300);
            primaryStage.setMaximized(true);

        } catch (Exception e) {
            System.err.println("Error loading FXML or CSS: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private LoginController prepareLoginScene() throws IOException {
        FXMLLoader loginLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Parent loginRoot = loginLoader.load();
        LoginController loginController = loginLoader.getController();
        loginScene = new Scene(loginRoot, 800, 600);

        loginScene.widthProperty().addListener((obs, oldVal, newVal) -> {
            loginController.backgroundImage.setFitWidth(newVal.doubleValue());
        });
        loginScene.heightProperty().addListener((obs, oldVal, newVal) -> {
            loginController.backgroundImage.setFitHeight(newVal.doubleValue());
        });

        return loginController;
    }

    private SignupController prepareSignUpScene() throws IOException {
        FXMLLoader signUpLoader = new FXMLLoader(getClass().getResource("signup-form.fxml"));
        Parent signUpRoot = signUpLoader.load();
        SignupController signupController = signUpLoader.getController();
        signupScene = new Scene(signUpRoot, 800, 600);

        signupScene.widthProperty().addListener((obs, oldVal, newVal) -> {
            signupController.backgroundImage.setFitWidth(newVal.doubleValue());
        });
        signupScene.heightProperty().addListener((obs, oldVal, newVal) -> {
            signupController.backgroundImage.setFitHeight(newVal.doubleValue());
        });

        return signupController;
    }

    private HelloController prepareDashboard() throws IOException {
        FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent dashboardRoot = dashboardLoader.load();
        HelloController dashboardController = dashboardLoader.getController();
        dashboardScene = new Scene(dashboardRoot, 800, 600);

        return dashboardController;
    }
}
