package com.example.vlearn.controller;

import com.example.vlearn.Utility;
import com.example.vlearn.network.ChatWindow;
import com.example.vlearn.network.NetworkClient;
import com.example.vlearn.util.TokenStorage;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;

public class HelloController {

    private Stage primaryStage;
    private Scene loginScene;
    private Scene dashboardScene;

    @FXML
    private VBox helpRequestPane;
    @FXML
    private Button btnShowRequests;
    @FXML
    private ListView<String> requestListView;
    @FXML
    private TextField txtHelpMessage;
    @FXML
    private Button btnSendHelp;

    private boolean isExpanded = false;
    private NetworkClient networkClient;
    private String username = "User_" + (int) (Math.random() * 1000);

    // ---------------- Scene Switching Setup ----------------
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
    public void initialize() {
        try {
            networkClient = new NetworkClient("localhost", 5000, username);
            networkClient.listen(this::handleServerMessage);
        } catch (IOException e) {
            showAlert("Connection Error", "Could not connect to server.");
        }

        requestListView.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setTooltip(null);
                    setOnMouseClicked(null);
                } else {
                    setText(item);
                    setTooltip(new Tooltip(item));

                    setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2) {
                            startPrivateChat(item);
                        }
                    });
                }

                setStyle("-fx-padding: 5px; -fx-border-color: lightgray; -fx-border-width: 0 0 1 0; -fx-font-weight: bold; -fx-background-color: #FFF8DC;");
            }
        });


    }

    private void startPrivateChat(String requestMessage) {
        // Parse the username from the request string
        String targetUser = requestMessage.split(":")[0].replace("Help requested by ", "").trim();

        // Open chat window
        Platform.runLater(() -> {
            ChatWindow chat = new ChatWindow(username, targetUser, networkClient);
            chat.show();
        });
    }

    private void handleServerMessage(String msg) {
        Platform.runLater(() -> {
            if (msg.contains("Help requested by")) {
                requestListView.getItems().add(msg);
            } else {
                System.out.println(msg);
            }
        });
    }

    @FXML
    public void onSendHelp(ActionEvent event) {
        String message = txtHelpMessage.getText();
        if (message == null || message.isEmpty()) message = "Need help!";
        networkClient.sendHelpRequest(message);
        txtHelpMessage.clear();
    }

    // ---------------- Help Pane ----------------
    @FXML
    public void toggleHelpRequests(ActionEvent event) {
        if (isExpanded) collapseHelpPane();
        else expandHelpPane();
        isExpanded = !isExpanded;
    }

    private void expandHelpPane() {
        helpRequestPane.setVisible(true);
        btnShowRequests.setText("Close");
        btnShowRequests.setStyle("-fx-background-color: linear-gradient(to right, #FF0000, #B22222);-fx-text-fill: #FFFFFF;");
        Timeline expand = new Timeline(new KeyFrame(Duration.seconds(0.3),
                new KeyValue(helpRequestPane.prefWidthProperty(), 250),
                new KeyValue(helpRequestPane.opacityProperty(), 1)));
        expand.play();
    }

    private void collapseHelpPane() {
        Timeline collapse = new Timeline(new KeyFrame(Duration.seconds(0.3),
                new KeyValue(helpRequestPane.prefWidthProperty(), 0),
                new KeyValue(helpRequestPane.opacityProperty(), 0)));
        collapse.setOnFinished(e -> {
            helpRequestPane.setVisible(false);
            btnShowRequests.setText("Helping Requests");
            btnShowRequests.setStyle("-fx-background-color: linear-gradient(to right, #00ff33, #12551b);-fx-text-fill: #FFFFFF;");
        });
        collapse.play();
    }

    // ---------------- Logout & Exit ----------------
    @FXML
    private void handleLogout(MouseEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Any unsaved progress will be lost.", ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Logout");
        alert.setHeaderText("Are you sure you want to logout?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            TokenStorage.clearToken();
            if (networkClient != null) networkClient.close();
            if (primaryStage != null && loginScene != null) {
                primaryStage.setScene(loginScene);
                primaryStage.setTitle("Login");
                primaryStage.setMaximized(false);
            }
        }
    }

    @FXML
    private void handleExit(MouseEvent event) {
        if (networkClient != null) networkClient.close();
        Platform.exit();
    }

    // ---------------- Open Sections ----------------
    @FXML
    private void openProgrammingBasics(ActionEvent event) throws IOException {
        Utility.switchScene(event, "/com/example/vlearn/programming-basics.fxml", "Programming Basics");
    }

    @FXML
    private void openDataStructure(ActionEvent event) throws IOException {
        Utility.switchScene(event, "/com/example/vlearn/dataStructure.fxml", "Data Structure");
    }

    @FXML
    private void openAlgorithm(ActionEvent event) throws IOException {
        Utility.switchScene(event, "/com/example/vlearn/algorithm.fxml", "Algorithm");
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
