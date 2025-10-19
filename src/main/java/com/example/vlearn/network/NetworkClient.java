package com.example.vlearn.network;

import java.io.*;
import java.net.Socket;
import java.util.function.Consumer;

public class NetworkClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String username;

    public NetworkClient(String host, int port, String username) throws IOException {
        this.username = username;
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Send username to server
        out.println(username);
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public void sendHelpRequest(String message) {
        sendMessage("/help " + message);
    }

    public void sendPrivateMessage(String targetUser, String msg) {
        out.println("PRIVATE:" + targetUser + ":" + msg);
    }


    public void listen(Consumer<String> onMessage) {
        Thread thread = new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null) {
                    onMessage.accept(msg);
                }
            } catch (IOException e) {
                System.out.println("Connection closed.");
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
