package com.example.vlearn.network;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ChatWindow extends Stage {
    private TextArea chatArea;
    private TextField inputField;
    private Button sendButton;

    private String fromUser;
    private String toUser;
    private NetworkClient client;

    public ChatWindow(String fromUser, String toUser, NetworkClient client) {
        this.fromUser = fromUser;
        this.toUser = toUser;
        this.client = client;

        setTitle("Chat with " + toUser);

        chatArea = new TextArea();
        chatArea.setEditable(false);

        inputField = new TextField();
        inputField.setPromptText("Type message...");

        sendButton = new Button("Send");
        sendButton.setOnAction(e -> sendMessage());

        VBox root = new VBox(5, chatArea, inputField, sendButton);
        root.setPrefSize(400, 300);

        setScene(new Scene(root));

        // Listen for messages from server
        client.listen(msg -> {
            if (msg.startsWith("Private from " + toUser)) {
                Platform.runLater(() -> chatArea.appendText(msg.replace("Private from " + toUser + ": ", "") + "\n"));
            }
        });
    }

    private void sendMessage() {
        String msg = inputField.getText();
        if (msg == null || msg.isEmpty()) return;
        client.sendPrivateMessage(toUser, msg); // Method in NetworkClient
        chatArea.appendText("Me: " + msg + "\n");
        inputField.clear();
    }
}
