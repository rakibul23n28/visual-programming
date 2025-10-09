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

public class AlgorithmVisualizerController {

    @FXML private AnchorPane simulationPane;

    @FXML private Button btnBubbleSort;
    @FXML private Button btnMergeSort;
    @FXML private Button btnDijkstra;
    @FXML private Button btnBFS;
    @FXML private Button btnDFS;

    private List<Button> sidebarButtons = new ArrayList<>();

    @FXML
    private void initialize() {
        // Collect all sidebar buttons for easy reset
        sidebarButtons.add(btnBubbleSort);
        sidebarButtons.add(btnMergeSort);
        sidebarButtons.add(btnDijkstra);
        sidebarButtons.add(btnBFS);
        sidebarButtons.add(btnDFS);
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        Utility.goBack(event);
    }

    @FXML
    private void openBubbleSort(ActionEvent event) throws IOException {
        setActiveButton(btnBubbleSort);
        loadSimulation("/com/example/vlearn/algorithm/BubbleSortVisualizer.fxml");
    }

    @FXML
    private void openMergeSort(ActionEvent event) throws IOException {
        setActiveButton(btnMergeSort);
        loadSimulation("/com/example/vlearn/algorithm/MergeSortVisualizer.fxml");
    }

    @FXML
    private void openDijkstra(ActionEvent event) throws IOException {
        setActiveButton(btnDijkstra);
        loadSimulation("/com/example/vlearn/algorithm/DijkstraVisualizer.fxml");
    }

    @FXML
    private void openBFS(ActionEvent event) throws IOException {
        setActiveButton(btnBFS);
        loadSimulation("/com/example/vlearn/algorithm/BFSVisualizer.fxml");
    }

    @FXML
    private void openDFS(ActionEvent event) throws IOException {
        setActiveButton(btnDFS);
        loadSimulation("/com/example/vlearn/algorithm/DFSVisualizer.fxml");
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
