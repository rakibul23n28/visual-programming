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
            requestUsername();
            broadcast(username + " joined the network.", this);

            String message;
            while ((message = reader.readLine()) != null) {
                handleMessage(message);
            }
        } catch (IOException e) {
            System.out.println(username + " disconnected.");
        } finally {
            disconnect();
        }
    }

    private void setupStreams() throws IOException {
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    private void requestUsername() throws IOException {
        writer.println("Enter your username:");
        username = reader.readLine();
        if (username == null || username.trim().isEmpty()) {
            username = "User_" + (int) (Math.random() * 1000);
        }
    }

    private void handleMessage(String message) {
        if (message.startsWith("PRIVATE:")) {
            // Format: PRIVATE:recipient:message
            String[] parts = message.split(":", 3);
            if (parts.length == 3) {
                String targetUser = parts[1];
                String privateMsg = parts[2];
                sendPrivateMessage(targetUser, privateMsg);
            } else {
                writer.println("Invalid private message format. Use: PRIVATE:recipient:message");
            }
        } else {
            // Broadcast all other messages as help requests
            broadcast("🆘 Help requested by " + username + ": " + message, this);
        }
    }

    private void broadcast(String msg, ClientHandler exclude) {
        for (ClientHandler client : clients) {
            if (client != exclude) {
                client.writer.println(msg);
            }
        }
    }

    private void sendPrivateMessage(String recipient, String msg) {
        boolean found = false;
        for (ClientHandler client : clients) {
            if (client.username.equalsIgnoreCase(recipient)) {
                client.writer.println("Private from " + username + ": " + msg);
                writer.println("To " + recipient + ": " + msg); // confirm sender
                found = true;
                break;
            }
        }
        if (!found) {
            writer.println("User " + recipient + " not found.");
        }
    }

    private void disconnect() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            clients.remove(this);
            broadcast(username + " left the network.", this);
        }
    }
}
