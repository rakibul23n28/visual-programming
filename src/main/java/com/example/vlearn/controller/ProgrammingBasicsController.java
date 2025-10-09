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

public class ProgrammingBasicsController {

    @FXML private AnchorPane simulationPane;

    @FXML private Button btnVariables;
    @FXML private Button btnOperators;
    @FXML private Button btnIfElse;
    @FXML private Button btnSwitch;
    @FXML private Button btnLoops;
    @FXML private Button btnFunctions;
    @FXML private Button btnArrays;
    @FXML private Button btnStrings;
    @FXML private Button btnIO;

    private List<Button> sidebarButtons = new ArrayList<>();

    @FXML
    private void initialize() {
        // collect all sidebar buttons for easy reset
        sidebarButtons.add(btnVariables);
        sidebarButtons.add(btnOperators);
        sidebarButtons.add(btnIfElse);
        sidebarButtons.add(btnSwitch);
        sidebarButtons.add(btnLoops);
        sidebarButtons.add(btnFunctions);
        sidebarButtons.add(btnArrays);
        sidebarButtons.add(btnStrings);
        sidebarButtons.add(btnIO);
    }

    @FXML
    private void goBack(ActionEvent event) throws IOException {
        Utility.goBack(event);
    }

    @FXML
    private void openVariables(ActionEvent event) throws IOException {
        setActiveButton(btnVariables);
        loadSimulation("/com/example/vlearn/topics/visualizer.fxml");
    }

    @FXML
    private void openOperators(ActionEvent event) throws IOException {
        setActiveButton(btnOperators);
        loadSimulation("/com/example/vlearn/topics/OperatorVisualizer.fxml");
    }

    @FXML
    private void openIfElse(ActionEvent event) throws IOException {
        setActiveButton(btnIfElse);
        loadSimulation("/com/example/vlearn/topics/ifelse_visualizer.fxml");
    }

    @FXML
    private void openSwitch(ActionEvent event) throws IOException {
        setActiveButton(btnSwitch);
        loadSimulation("/com/example/vlearn/topics/SwitchStatementVisualizer.fxml");
    }

    @FXML
    private void openLoops(ActionEvent event) throws IOException {
        setActiveButton(btnLoops);
        loadSimulation("/com/example/vlearn/topics/loopsVisualizer.fxml");
    }

    @FXML
    private void openFunctions(ActionEvent event) throws IOException {
        setActiveButton(btnFunctions);
        loadSimulation("/com/example/vlearn/topics/FunctionVisualizer.fxml");
    }

    @FXML
    private void openArrays(ActionEvent event) throws IOException {
        setActiveButton(btnArrays);
        loadSimulation("/com/example/vlearn/topics/ArrayVisualizer.fxml");
    }

    @FXML
    private void openStrings(ActionEvent event) throws IOException {
        setActiveButton(btnStrings);
        loadSimulation("/com/example/vlearn/topics/StringVisualizer.fxml");
    }

    @FXML
    private void openIO(ActionEvent event) throws IOException {
        setActiveButton(btnIO);
        loadSimulation("/com/example/vlearn/topics/Input_Output_simulator.fxml");
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
