package com.example.vlearn;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Utility {

    // Make this public static so other classes can call it directly
    public static void switchScene(ActionEvent event, String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(Utility.class.getResource(fxmlPath));
        Scene newScene = new Scene(loader.load());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        // Save current size
        double width = stage.getWidth();
        double height = stage.getHeight();

        // Apply new scene
        stage.setScene(newScene);
        stage.setTitle(title);

        // Restore previous size
        stage.setWidth(width);
        stage.setHeight(height);

        stage.show();
    }


    public static void goBack(ActionEvent event) throws IOException {
        switchScene(event, "/com/example/vlearn/hello-view.fxml", "Learning by Visual");
    }
}