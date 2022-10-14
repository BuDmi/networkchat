package org.example.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Client extends Thread {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
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
            out.println("Please set your name to enter in chat: ");
            this.name = in.readLine();
            out.println("Welcome in our chat, " + ANSI_RED + name + ANSI_RESET + " !");
            chat.incrementClientsNum();
            this.start();
            clientNotifier.notifyClients("User " + ANSI_RED + name + ANSI_RESET + " joined the chat", this);
        } catch (IOException e) {
            System.out.println("Couldn't initialize socket streams");
            this.interrupt();
        }
    }

    @Override
    public void run() {
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
        return ANSI_BLUE + time + ANSI_RED + " " + name + ANSI_RESET +": " + message;
    }

    @Override
    public void interrupt() {
        clientNotifier.notifyClients("User " + name + " left the chat", this);
        super.interrupt();
    }

    public void notifyInChat(String message) {
        newMessage = message;
    }
}
