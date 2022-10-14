package org.example.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientNotifier {
    private final List<Client> clients = new ArrayList<>();
    private Chat chat;

    public ClientNotifier(Chat chat) {
        this.chat = chat;
    }

    enum RES {EXISTED_CLIENT, SUCCESS}

    public void addNewClient(Socket socket) {
        clients.add(new Client(socket, chat, this));
    }

    public void deleteClient(Client client) {
        clients.remove(client);
    }

    public void notifyClients(String message, Client curClient) {
        for (Client client: clients) {
            if (client != curClient) {
                client.notifyInChat(message);
            }
        }
    }
}
