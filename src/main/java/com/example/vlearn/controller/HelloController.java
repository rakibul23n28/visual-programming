package com.example.vlearn.controller;

import com.example.vlearn.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class HelloController {

    @FXML
    private void openProgrammingBasics(ActionEvent event) throws IOException {
        Utility.switchScene(event, "/com/example/vlearn/programming-basics.fxml", "Programming Basics");
    }

    @FXML
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
}
