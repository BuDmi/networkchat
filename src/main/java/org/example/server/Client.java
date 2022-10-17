package org.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Client extends Thread {
    private final Chat chat;
    private final ClientNotifier clientNotifier;
    private PrintWriter serverMessageWriter;
    private BufferedReader clientMessageReader;
    private volatile String name;
    private volatile String newMessage = "";
    public Client(Socket socket, Chat chat, ClientNotifier clientNotifier) {
        this.chat = chat;
        this.clientNotifier = clientNotifier;
        try {
            serverMessageWriter = new PrintWriter(socket.getOutputStream(), true);
            clientMessageReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.start();
        } catch (IOException e) {
            System.out.println("Couldn't initialize socket streams");
            this.interrupt();
        }
    }

    public String getClientName() {
        return name;
    }

    @Override
    public void run() {
        setClientName();
        serverMessageWriter.println("Welcome in our chat, `" + name + "` !");
        clientNotifier.notifyClients("User `" + name + "` joined the chat", this);
        while (!isInterrupted()) {
            try {
                if (clientMessageReader.ready()) {
                    registerMessageFromCurrentClient();
                } else {
                    publishMessageFromAnotherClients();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void publishMessageFromAnotherClients() {
        if (!newMessage.isEmpty()) {
            serverMessageWriter.println(newMessage);
            newMessage = "";
        }
    }

    private void registerMessageFromCurrentClient() {
        try {
            String message = clientMessageReader.readLine();
            if (message.equals("/exit")) {
                clientNotifier.deleteClient(this);
                this.interrupt();
            } else {
                chat.receiveMessage(formChatMessage(message));
                clientNotifier.notifyClients(chat.sendMessage(), this);
            }
        } catch (IOException e) {
            System.out.println("Error on receiving message for client " + name);
        }
    }

    private void setClientName() {
        serverMessageWriter.println("Please set your name to enter in chat: ");
        try {
            this.name = clientMessageReader.readLine();
        } catch (IOException e) {
            System.out.println("Couldn't get name");
            this.interrupt();
        }
        while (clientNotifier.checkForExistedClientName(name)) {
            serverMessageWriter.println("The name `" + name + "` is already exist in chat. Please, set another name");
            try {
                this.name = clientMessageReader.readLine();
            } catch (IOException e) {
                System.out.println("Couldn't get name");
                this.interrupt();
            }
        }
    }

    private String formChatMessage(String message) {
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        return time + " `" + name + "`: " + message;
    }

    @Override
    public void interrupt() {
        clientNotifier.notifyClients("User `" + name + "` left the chat", this);
        super.interrupt();
    }

    public void notifyInChat(String message) {
        newMessage = message;
    }
}
