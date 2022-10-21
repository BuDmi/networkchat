package org.example.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientNotifier {
    private final List<Client> clients = new ArrayList<>();
    private final Chat chat;

    public ClientNotifier(Chat chat) {
        this.chat = chat;
    }

    public void addNewClient(Socket socket) {
        clients.add(new Client(socket, chat, this));
    }

    public Boolean checkForExistedClientName(Client curClient) {
        synchronized (clients) {
            String newClientName = curClient.getClientName();
            for(Client client: clients) {
                if (client == curClient && !client.isClientEnteredInChat()) {
                    continue;
                }
                if (client.getClientName().equals(newClientName)) {
                    return true;
                }
            }
            return false;
        }
    }

    public void deleteClient(Client client) {
        clients.remove(client);
    }

    public void notifyClients(String message, Client curClient) {
        synchronized (clients) {
            chat.logMessage(message);
            for (Client client : clients) {
                if (client != curClient && client.isClientEnteredInChat()) {
                    client.notifyInChat(message);
                }
            }
        }
    }
}
