package org.example.server;

import org.example.FileUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Server {

    private final List<Client> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        Map<String, String> settings = FileUtils.readSettingsFromFile("src/main/resources/", "settings.txt");
        int port = Integer.parseInt(settings.get("port"));

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            Chat chat = new Chat();
            ClientNotifier clientNotifier = new ClientNotifier(chat);

            System.out.println("Server started on port " + port);
            while (true) {
                clientNotifier.addNewClient(serverSocket.accept());
                System.out.println("New client connected");
            }
        }
    }
}
