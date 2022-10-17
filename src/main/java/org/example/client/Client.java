package org.example.client;

import org.example.FileUtils;
import org.example.logger.Logger;
import org.example.logger.TextFileLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class Client {

    public static void main(String[] args) throws IOException {
        String path = "src/main/resources/client/";
        String settingsFileName = "settings.txt";
        Map<String, String> settings = FileUtils.readSettingsFromFile(path, settingsFileName);
        String host = settings.get("host");
        int port = Integer.parseInt(settings.get("port"));

        System.out.println("Connecting to the chat on `" + host + ":" + port + "`");
        try(Socket clientSocket = new Socket(host, port);
            PrintWriter clientMessageWriter = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            try(BufferedReader serverMessageReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                BufferedReader clientInputReader = new BufferedReader(new InputStreamReader(System.in))
            ) {
                String logFileName = "file.log";
                Logger logger = new TextFileLogger(path, logFileName);
                interact(clientMessageWriter, serverMessageReader, clientInputReader, logger);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void interact(
        PrintWriter clientMessageWriter, BufferedReader serverMessageReader, BufferedReader clientInputReader, Logger logger
    ) {
        String curClientMessage = "";
        String otherClientsName;
        boolean isEnteredToChat = false;
        String keyWorkOfChatEntrance = "Welcome";
        String keyWorkToExitFromChat = "/exit";
        while(!curClientMessage.equals(keyWorkToExitFromChat)) {
            try {
                if (clientInputReader.ready()) {
                    curClientMessage = clientInputReader.readLine();
                    clientMessageWriter.println(curClientMessage);
                    if (isEnteredToChat && !curClientMessage.equals(keyWorkToExitFromChat)) {
                        logger.logNewMessage(curClientMessage);
                    }
                } else {
                    if (serverMessageReader.ready()) {
                        otherClientsName = serverMessageReader.readLine();
                        System.out.println(otherClientsName);
                        if (isEnteredToChat) {
                            logger.logNewMessage(otherClientsName);
                        } else {
                            isEnteredToChat = otherClientsName.contains(keyWorkOfChatEntrance);
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
