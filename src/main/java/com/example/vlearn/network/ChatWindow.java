package com.example.vlearn.network;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class ChatWindow extends Stage {

    private VBox chatBox;         // Container for chat messages
    private ScrollPane scrollPane; // Scroll pane wrapping chatBox
    private TextField inputField;  // Text input
    private Button sendButton;     // Send button
    private Button micButton;      // Microphone button
    private Button speakerButton;  // Speaker button

    private String fromUser;
    private String toUser;
    private NetworkClient client;

    private Consumer<String> listener;

    public ChatWindow(String fromUser, String toUser, NetworkClient client) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.client = client;

        setTitle("Chat with " + toUser);

        // Chat container
        chatBox = new VBox(5);
        chatBox.setPadding(new Insets(10));

        // ScrollPane for chat messages
        scrollPane = new ScrollPane(chatBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background: #f0f0f0;");

        // Input field
        inputField = new TextField();
        inputField.setPromptText("Type message...");
        inputField.setPrefHeight(30);

        // Send button
        sendButton = new Button("Send");
        sendButton.setPrefHeight(30);
        sendButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");

        // Microphone button (disabled)
        micButton = new Button("\uD83C\uDFA4"); // 🎤 unicode icon
        micButton.setPrefHeight(30);
        micButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-weight: bold;");
        micButton.setDisable(true); // disabled

        // Speaker button (disabled)
        speakerButton = new Button("\uD83D\uDD0A"); // 🔊 unicode icon
        speakerButton.setPrefHeight(30);
        speakerButton.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-weight: bold;");
        speakerButton.setDisable(true); // disabled

        // Input area HBox
        HBox inputArea = new HBox(5, micButton, speakerButton, inputField, sendButton);
        inputArea.setPadding(new Insets(5));
        inputArea.setAlignment(Pos.CENTER);
        HBox.setHgrow(inputField, Priority.ALWAYS);

        // Root VBox
        VBox root = new VBox(5, scrollPane, inputArea);
        root.setPrefSize(500, 400);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        setScene(new Scene(root));

        // Send message on click or Enter
        sendButton.setOnAction(e -> sendMessage());
        inputField.setOnAction(e -> sendMessage());

        // Listener for incoming messages
        listener = message -> {
            if (message.startsWith("Private from") && isPrivateMessageForThisChat(message)) {
                String sender = extractSender(message);
                if (sender.equalsIgnoreCase(fromUser)) return; // ignore own messages
                String msgText = extractMessage(message);
                Platform.runLater(() -> addMessage(sender, msgText, false));
            }
        };
        client.addListener(listener);

        // Remove listener when window closes
        setOnCloseRequest(e -> client.removeListener(listener));
    }

    private void sendMessage() {
        String msg = inputField.getText();
        if (msg == null || msg.isEmpty()) return;
        client.sendPrivateMessage(toUser, msg);
        addMessage("Me", msg, true);
        inputField.clear();
    }

    private void addMessage(String sender, String msg, boolean isMe) {
        HBox messageBox = new HBox();
        messageBox.setPadding(new Insets(5, 10, 5, 10));

        Text text = new Text(msg);
        text.setFill(Color.WHITE);
        text.setWrappingWidth(250);

        Text senderText = new Text(isMe ? sender : "Helper");
        senderText.setFill(Color.LIGHTGRAY);

        VBox messageContainer = new VBox(senderText, text);
        messageContainer.setBackground(new Background(new BackgroundFill(
                isMe ? Color.DODGERBLUE : Color.DARKGRAY,
                new CornerRadii(10),
                Insets.EMPTY
        )));
        messageContainer.setPadding(new Insets(5));
        messageContainer.setMaxWidth(300);

        messageBox.getChildren().add(messageContainer);
        messageBox.setAlignment(isMe ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

        chatBox.getChildren().add(messageBox);

        // Scroll to bottom automatically
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }

    private boolean isPrivateMessageForThisChat(String message) {
        String sender = extractSender(message);
        return sender.equalsIgnoreCase(toUser);
    }

    private String extractSender(String message) {
        String[] parts = message.split(":", 2);
        return parts[0].replace("Private from", "").trim();
    }

    private String extractMessage(String message) {
        String[] parts = message.split(":", 2);
        return parts.length > 1 ? parts[1].trim() : "";
    }
}
