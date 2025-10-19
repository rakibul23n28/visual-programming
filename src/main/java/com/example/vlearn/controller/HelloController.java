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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    private VBox requestVBox; // Used instead of ListView for displaying requests
    @FXML
    private TextField txtHelpMessage;
    @FXML
    private Button btnSendHelp;

    private boolean isExpanded = false;
    private NetworkClient networkClient;
    private String username = "User_" + (int) (Math.random() * 1000);

    // Track open private chats
    private Map<String, ChatWindow> openChats = new HashMap<>();

    // ---------------- Scene Switching ----------------
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
            networkClient.listenToServer(); // Start single listening thread

            // Subscribe to all messages from server
            networkClient.addListener(this::handleServerMessage);

        } catch (IOException e) {
            showAlert("Connection Error", "Could not connect to server.");
        }
    }

    // ---------------- Server Message Handler ----------------
    private void handleServerMessage(String msg) {
        Platform.runLater(() -> {
            if (msg.contains("Help requested by")) {
                addHelpRequest(msg);
            } else if (msg.startsWith("Private from")) {
                String sender = extractSender(msg);
                if (!openChats.containsKey(sender)) {
                    ChatWindow chat = new ChatWindow(username, sender, networkClient);
                    openChats.put(sender, chat);
                    chat.setOnCloseRequest(e -> openChats.remove(sender));
                    chat.show();
                }
            } else if (msg.startsWith("USERLIST:")) {
                String[] users = msg.substring(9).split(",");
            } else {
                System.out.println("[Broadcast] " + msg);
            }
        });
    }

    // ---------------- Help Request UI with Countdown ----------------
    private void addHelpRequest(String requestMessage) {
        Label requestLabel = new Label(requestMessage);
        Label countdownLabel = new Label("60s"); // countdown display
        Button joinButton = new Button("Join Chat");

        requestLabel.setWrapText(true);
        requestLabel.setTooltip(new Tooltip(requestMessage));
        requestLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #FFF8DC; -fx-padding: 5px 0 5px 5px;");
        requestLabel.setMaxWidth(Double.MAX_VALUE);

        countdownLabel.setStyle("-fx-text-fill: #FFD700; -fx-font-size: 10px; -fx-padding: 0 5 0 5;");
        joinButton.setStyle("-fx-background-color: #FF69B4; -fx-text-fill: white; -fx-font-size: 10px; -fx-padding: 3 8 3 8; -fx-background-radius: 5; -fx-cursor: hand;");

        joinButton.setOnAction(e -> {
            String targetUser = requestMessage.split(":")[0].replace("Help requested by ", "").trim();
            networkClient.sendPrivateMessage(targetUser, "I see your request. I'm here to help!");
            if (!openChats.containsKey(targetUser)) {
                ChatWindow chat = new ChatWindow(username, targetUser, networkClient);
                openChats.put(targetUser, chat);
                chat.setOnCloseRequest(ev -> openChats.remove(targetUser));
                chat.show();
            }
        });

        HBox requestItem = new HBox(requestLabel, countdownLabel, joinButton);
        requestItem.setSpacing(5);
        requestItem.setStyle("-fx-background-color: #1a1a3f; -fx-border-color: lightgray; -fx-border-width: 0 0 1 0; -fx-alignment: CENTER_LEFT; -fx-padding: 0 5 0 0;");

        HBox.setHgrow(requestLabel, Priority.ALWAYS);

        requestVBox.getChildren().add(requestItem);

        // Countdown timeline
        Timeline countdown = new Timeline();
        for (int i = 60; i >= 0; i--) {
            int seconds = i;
            countdown.getKeyFrames().add(new KeyFrame(Duration.seconds(60 - i), ev -> countdownLabel.setText(seconds + "s")));
        }
        countdown.setOnFinished(ev -> requestVBox.getChildren().remove(requestItem));
        countdown.play();
    }

    @FXML
    public void onSendHelp(ActionEvent event) {
        String message = txtHelpMessage.getText();
        if (message == null || message.isEmpty()) message = "Need help!";
        networkClient.sendHelpRequest(message);
        txtHelpMessage.clear();
    }

    // ---------------- Help Pane Animation ----------------
    @FXML
    public void toggleHelpRequests(ActionEvent event) {
        helpRequestPane.toFront();
        btnShowRequests.toFront();
        if (isExpanded) collapseHelpPane();
        else expandHelpPane();
        isExpanded = !isExpanded;
    }

    private void expandHelpPane() {
        helpRequestPane.setVisible(true);
        btnShowRequests.setText("Close");
        btnShowRequests.setStyle("-fx-background-color: linear-gradient(to right, #FF0000, #B22222);-fx-text-fill: #FFFFFF;");
        Timeline expand = new Timeline(new KeyFrame(Duration.seconds(0.3),
                new KeyValue(helpRequestPane.prefWidthProperty(), 400),
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
            btnShowRequests.setStyle("-fx-font: bold;-fx-background-color: linear-gradient(to right, #00ff33, #12551b);-fx-text-fill: #FFFFFF;");
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to exit?\nAny unsaved progress will be lost.",
                ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Exit");
        alert.setHeaderText("Confirm Exit");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            if (networkClient != null) networkClient.close();
            Platform.exit();
        }
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

    private String extractSender(String message) {
        String[] parts = message.split(":", 2);
        return parts[0].replace("Private from", "").trim();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
