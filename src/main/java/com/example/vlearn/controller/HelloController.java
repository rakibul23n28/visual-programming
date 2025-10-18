package com.example.vlearn.controller;

import com.example.vlearn.Utility;
<<<<<<< HEAD
import com.example.vlearn.util.TokenStorage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class HelloController {

    private Stage primaryStage;
    private Scene loginScene;
    private Scene dashboardScene;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setLoginScene(Scene scene) {
        this.loginScene = scene;
    }

    public void setDashboardScene(Scene scene) {
        this.dashboardScene = scene;
    }

    @FXML
    private void handleLogout(javafx.scene.input.MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Any unsaved progress will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            TokenStorage.clearToken();
            if (primaryStage != null && loginScene != null) {
                primaryStage.setScene(loginScene);
                primaryStage.setTitle("Login");
                primaryStage.setMaximized(false);
            }
        }

    }

    @FXML
    private void handleExit(javafx.scene.input.MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Confirmation");
        alert.setHeaderText("Are you sure you want to exit?");
        alert.setContentText("Any unsaved progress will be lost.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }

    // Your other navigation methods...
=======
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class HelloController {

>>>>>>> f1b259cc68276d4a9dd787fafa2d358bd9478c3e
    @FXML
    private void openProgrammingBasics(ActionEvent event) throws IOException {
        Utility.switchScene(event, "/com/example/vlearn/programming-basics.fxml", "Programming Basics");
    }

    @FXML
<<<<<<< HEAD
    private void openDataStructure(ActionEvent event) throws IOException {
        Utility.switchScene(event, "/com/example/vlearn/dataStructure.fxml", "Data Structure");
    }

    @FXML
    private void openAlgorithm(ActionEvent event) throws IOException {
        Utility.switchScene(event, "/com/example/vlearn/algorithm.fxml", "Algorithm");
    }


=======
    private void openDataStructure(ActionEvent event) {
        // TODO: implement Data Structure page
    }

    @FXML
    private void openAlgorithm(ActionEvent event) {
        // TODO: implement Algorithm page
    }

    @FXML
    private void openAIVisual(ActionEvent event) {
        // TODO: implement AI Visual page
    }

    private void switchScene(ActionEvent event, String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Scene scene = new Scene(loader.load(), 650, 450);
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
    }
>>>>>>> f1b259cc68276d4a9dd787fafa2d358bd9478c3e
}
