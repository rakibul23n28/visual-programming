package com.example.vlearn.controller;

import com.example.vlearn.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataStructureController {

    @FXML private AnchorPane simulationPane;

    @FXML private Button btnArray;
    @FXML private Button btnLinkedList;
    @FXML private Button btnStack;
    @FXML private Button btnQueue;
    @FXML private Button btnTree;
    @FXML private Button btnGraph;
    @FXML private Button btnHashMap;

    private List<Button> sidebarButtons = new ArrayList<>();

    @FXML
    private void initialize() {
        // collect all sidebar buttons for easy reset
        sidebarButtons.add(btnArray);
        sidebarButtons.add(btnLinkedList);
        sidebarButtons.add(btnStack);
        sidebarButtons.add(btnQueue);
        sidebarButtons.add(btnTree);
        sidebarButtons.add(btnGraph);
        sidebarButtons.add(btnHashMap);
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        Utility.goBack(event);
    }

    @FXML
    private void openArray(ActionEvent event) throws IOException {
        setActiveButton(btnArray);
        loadSimulation("/com/example/vlearn/dataStructure/ArrayVisualizer.fxml");
    }

    @FXML
    private void openLinkedList(ActionEvent event) throws IOException {
        setActiveButton(btnLinkedList);
        loadSimulation("/com/example/vlearn/dataStructure/LinkedListVisualizer.fxml");
    }

    @FXML
    private void openStack(ActionEvent event) throws IOException {
        setActiveButton(btnStack);
        loadSimulation("/com/example/vlearn/dataStructure/stackVisualizer.fxml");
    }

    @FXML
    private void openQueue(ActionEvent event) throws IOException {
        setActiveButton(btnQueue);
        loadSimulation("/com/example/vlearn/dataStructure/QueueVisualizer.fxml");
    }

    @FXML
    private void openTree(ActionEvent event) throws IOException {
        setActiveButton(btnTree);
        loadSimulation("/com/example/vlearn/dataStructure/TreeVisualizer.fxml");
    }

    @FXML
    private void openGraph(ActionEvent event) throws IOException {
        setActiveButton(btnGraph);
        loadSimulation("/com/example/vlearn/dataStructure/GraphVisualizer.fxml");
    }

    @FXML
    private void openHashMap(ActionEvent event) throws IOException {
        setActiveButton(btnHashMap);
        loadSimulation("/com/example/vlearn/dataStructure/HashMapVisualizer.fxml");
    }


    // ✅ Load content into simulationPane
    private void loadSimulation(String fxmlPath) throws IOException {
        simulationPane.getChildren().clear();
        Node content = FXMLLoader.load(getClass().getResource(fxmlPath));
        AnchorPane.setTopAnchor(content, 0.0);
        AnchorPane.setBottomAnchor(content, 0.0);
        AnchorPane.setLeftAnchor(content, 0.0);
        AnchorPane.setRightAnchor(content, 0.0);
        simulationPane.getChildren().add(content);
    }

    // ✅ Highlight active button
    private void setActiveButton(Button activeButton) {
        for (Button btn : sidebarButtons) {
            btn.setStyle(""); // reset style
        }
        activeButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
    }

}
