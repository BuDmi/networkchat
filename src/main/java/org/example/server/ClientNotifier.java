package org.example.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ClientNotifier {
    private final List<Client> clients = new ArrayList<>();
    private final Chat chat;

    public ClientNotifier(Chat chat) {
        this.chat = chat;
    }

    public void addNewClient(Socket socket) {
        clients.add(new Client(socket, chat, this));
    }

    public Boolean checkForExistedClientName(String newClientName) {
        return clients.stream().filter(client -> client.getClientName().equals(newClientName)).count() > 1;
    }

    public void deleteClient(Client client) {
        clients.remove(client);
    }

    public void notifyClients(String message, Client curClient) {
        chat.logMessage(message);
        for (Client client: clients) {
            if (client != curClient) {
                client.notifyInChat(message);
            }
        }
    }
}
