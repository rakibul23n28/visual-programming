package com.example.vlearn.server;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientHandler extends Thread {

    private final Socket socket;
    private final List<ClientHandler> clients;
    private BufferedReader reader;
    private PrintWriter writer;
    private String username;

    public ClientHandler(Socket socket, List<ClientHandler> clients) {
        this.socket = socket;
        this.clients = clients;
    }

    @Override
    public void run() {
        try {
            setupStreams();

            // ✅ First message should be the username from client
            username = reader.readLine();
            if (username == null || username.trim().isEmpty()) {
                username = "User_" + (int) (Math.random() * 1000);
            }

            System.out.println(username + " connected.");
            broadcast(username + " joined the network.");

            // ✅ Waiting for client messages
            String message;
            while ((message = reader.readLine()) != null) {
                handleMessage(message);
            }

        } catch (IOException e) {
            System.out.println(username + " disconnected due to error.");
        } finally {
            disconnect();
        }
    }

    private void setupStreams() throws IOException {
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    private void handleMessage(String message) {
        if (message.startsWith("/help")) {  // ✅ Help request
            String helpText = message.substring(5).trim();
            if (helpText.isEmpty()) helpText = "Need help!";
            broadcast("Help requested by " + username + ": " + helpText);
        }
        else if (message.startsWith("PRIVATE:")) { // ✅ Private message
            String[] parts = message.split(":", 3);
            if (parts.length == 3) {
                sendPrivateMessage(parts[1], parts[2]);
            } else {
                writer.println("⚠ Invalid format! Use: PRIVATE:username:message");
            }
        }
        else { // ✅ Normal chat message
            broadcast(username + ": " + message);
        }
    }

    // ✅ Send message to all connected clients
    private void broadcast(String msg) {
        for (ClientHandler client : clients) {
            client.writer.println(msg);
        }
        System.out.println("[Broadcast] " + msg);
    }

    // ✅ Send private message to specific user
    private void sendPrivateMessage(String recipient, String msg) {
        System.out.println("[Send] " + recipient);
        System.out.println(msg + " message" + clients.toString());
        for (ClientHandler client : clients) {
            if (client.username.equalsIgnoreCase(recipient.trim())) {
                System.out.println(client.username);
                client.writer.println("Private from " + username + ": " + msg);
                writer.println("✅ Sent to " + recipient + ": " + msg);
                return;
            }
        }
        writer.println("⚠ User " + recipient + " not found.");
    }

    // ✅ Handle clean disconnection
    private void disconnect() {
        try {
            if (!socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clients.remove(this);
            broadcast(username + " left the network.");
            System.out.println(username + " disconnected.");
        }
    }
}
