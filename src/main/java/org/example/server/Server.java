package org.example.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private final List<Client> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        System.out.println("Server started");
        int port = 8080;

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            Chat chat = new Chat();
            ClientNotifier clientNotifier = new ClientNotifier(chat);

            while (true) {
                clientNotifier.addNewClient(serverSocket.accept());
                System.out.println("New client connected");
            }
        }
    }
}
