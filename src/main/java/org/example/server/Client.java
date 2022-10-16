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
    private PrintWriter out;
    private BufferedReader in;
    private volatile String name;
    private String newMessage = "";
    public Client(Socket socket, Chat chat, ClientNotifier clientNotifier) {
        this.chat = chat;
        this.clientNotifier = clientNotifier;
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.start();
        } catch (IOException e) {
            System.out.println("Couldn't initialize socket streams");
            this.interrupt();
        }
    }

    @Override
    public void run() {
        out.println("Please set your name to enter in chat: ");
        try {
            this.name = in.readLine();
        } catch (IOException e) {
            System.out.println("Couldn't get name");
            this.interrupt();
        }
        out.println("Welcome in our chat, `" + name + "` !");
        chat.incrementClientsNum();
        clientNotifier.notifyClients("User `" + name + "` joined the chat", this);
        while (!isInterrupted()) {
            try {
                if (in.ready()) {
                    try {
                        String message = in.readLine();
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
                } else {
                    if (!newMessage.isEmpty()) {
                        out.println(newMessage);
                        newMessage = "";
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
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
