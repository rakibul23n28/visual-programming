package com.example.vlearn.network;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NetworkClient {

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    private List<Consumer<String>> listeners = new ArrayList<>();
    private boolean listening = false;

    public NetworkClient(String host, int port, String username) throws IOException {
        this.username = username;
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Send username as first message to server
        out.println(username);
    }

    // ---------------- Messaging ----------------

    public void sendHelpRequest(String message) {
        out.println("/help " + message);
        System.out.println("Help request sent: " + message);
    }

    public void sendPrivateMessage(String targetUser, String msg) {
        out.println("PRIVATE:" + targetUser + ":" + msg);
    }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    // ---------------- Listening ----------------

    // Add a listener (can be ChatWindow or HelloController)
    public void addListener(Consumer<String> listener) {
        listeners.add(listener);
    }

    public void removeListener(Consumer<String> listener) {
        listeners.remove(listener);
    }

    // Start a single background thread to listen to server messages
    public void listenToServer() {
        if (listening) return; // only start once
        listening = true;

        Thread thread = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    final String message = msg;
                    for (Consumer<String> listener : listeners) {
                        listener.accept(message);
                    }
                }
            } catch (IOException e) {
                System.out.println("Connection closed.");
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    // ---------------- Close ----------------
    public void close() {
        try {
            listening = false;
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
