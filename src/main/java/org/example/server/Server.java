package org.example.server;

import org.example.FileUtils;
import org.example.logger.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Map;

public class Server {
    public static void main(String[] args) throws IOException {
        String path = "src/main/resources/server/";
        String settingsFileName = "settings.txt";
        Map<String, String> settings = FileUtils.readSettingsFromFile(path, settingsFileName);
        int port = Integer.parseInt(settings.get("port"));

        try(ServerSocket serverSocket = new ServerSocket(port)) {
            String logFileName = "file.log";
            Logger logger = new TextFileLogger(path, logFileName);
            Chat chat = new Chat(logger);
            ClientNotifier clientNotifier = new ClientNotifier(chat);

            System.out.println("Server started on port " + port);
            while (true) {
                clientNotifier.addNewClient(serverSocket.accept());
                System.out.println("New client connected");
            }
        }
    }
}
