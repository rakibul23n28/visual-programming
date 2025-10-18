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
            LoginController loginController1 = prepareLoginScene();
            SignupController signupController = prepareSignUpScene();
            HelloController dashboardController = prepareDashboard();

<<<<<<< HEAD
            // Set stage and scenes for HelloController
            dashboardController.setPrimaryStage(primaryStage);
            dashboardController.setLoginScene(loginScene);
            dashboardController.setDashboardScene(dashboardScene);

=======
>>>>>>> f1b259cc68276d4a9dd787fafa2d358bd9478c3e
            loginController1.setPrimaryStage(primaryStage);
            loginController1.setSignUpScene(signupScene);
            loginController1.setDashboardScene(dashboardScene);

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
        }
    }

    private LoginController prepareLoginScene() throws IOException {
        FXMLLoader loginLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
        Parent loginRoot = loginLoader.load();
        LoginController loginController1 = loginLoader.getController();
        loginScene = new Scene(loginRoot, 800, 600);
//        loginScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/login.css")).toExternalForm());
        loginScene.widthProperty().addListener((obs, oldVal, newVal) -> {
            loginLoader.<LoginController>getController().backgroundImage.setFitWidth(newVal.doubleValue());
        });
        loginScene.heightProperty().addListener((obs, oldVal, newVal) -> {
            loginLoader.<LoginController>getController().backgroundImage.setFitHeight(newVal.doubleValue());
        });

        return loginController1;
    }


    private  SignupController prepareSignUpScene() throws IOException {
        FXMLLoader signUpLoader = new FXMLLoader(getClass().getResource("signup-form.fxml"));
        Parent signUpRoot = signUpLoader.load();
        SignupController signupController = signUpLoader.getController();
<<<<<<< HEAD
        signupScene = new Scene(signUpRoot, 800,600);
=======
        signupScene = new Scene(signUpRoot);
>>>>>>> f1b259cc68276d4a9dd787fafa2d358bd9478c3e
//        signupScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("css/login.css")).toExternalForm());
        signupScene.widthProperty().addListener((obs, oldVal, newVal) -> {
            signUpLoader.<SignupController>getController().backgroundImage.setFitWidth(newVal.doubleValue());
        });
        signupScene.heightProperty().addListener((obs, oldVal, newVal) -> {
            signUpLoader.<SignupController>getController().backgroundImage.setFitHeight(newVal.doubleValue());
        });

        return signupController;
    }

    private HelloController prepareDashboard() throws IOException {
        FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
        Parent dashboardRoot = dashboardLoader.load();
        HelloController dashboardController = dashboardLoader.getController();
<<<<<<< HEAD
        dashboardScene = new Scene(dashboardRoot, 800,600);
=======
        dashboardScene = new Scene(dashboardRoot);
>>>>>>> f1b259cc68276d4a9dd787fafa2d358bd9478c3e
//        dashboardScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());
//        dashboardScene.widthProperty().addListener((obs, oldVal, newVal) -> {
//            dashboardLoader.<DashboardController>getController().backgroundImage.setFitWidth(newVal.doubleValue());
//        });
//        dashboardScene.heightProperty().addListener((obs, oldVal, newVal) -> {
//            dashboardLoader.<DashboardController>getController().backgroundImage.setFitHeight(newVal.doubleValue());
//        });

        return dashboardController;
    }
}
