package com.example.vlearn;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.event.ActionEvent;
import javafx.scene.Node;

import java.io.IOException;

public class Utility {

    /**
     * Switch scene using an ActionEvent from a Node (Button)
     */
    public static void switchScene(ActionEvent event, String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(Utility.class.getResource(fxmlPath));
        Scene newScene = new Scene(loader.load());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        double width = stage.getWidth();
        double height = stage.getHeight();

        stage.setScene(newScene);
        stage.setTitle(title);

        stage.setWidth(width);
        stage.setHeight(height);

        stage.show();
    }

    /**
     * Switch scene safely without a Node (for MenuItem or anywhere)
     */
    public static void switchSceneFromAny(String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(Utility.class.getResource(fxmlPath));
        Scene newScene = new Scene(loader.load());

        // Get the currently focused window
        Window focusedWindow = Stage.getWindows().stream()
                .filter(Window::isFocused)
                .findFirst()
                .orElse(null);

        if (focusedWindow instanceof Stage stage) {
            double width = stage.getWidth();
            double height = stage.getHeight();

            stage.setScene(newScene);
            stage.setTitle(title);

            stage.setWidth(width);
            stage.setHeight(height);

            stage.show();
        }
    }

    /**
     * Go back to main Hello view (for Back buttons)
     */
    public static void goBack(ActionEvent event) throws IOException {
        switchScene(event, "/com/example/vlearn/hello-view.fxml", "Learning by Visual");
    }
}
