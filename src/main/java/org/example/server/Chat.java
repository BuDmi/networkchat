package org.example.server;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private final List<String> messages = new ArrayList<>();

    public synchronized String sendMessage() {
        while (messages.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return messages.remove(messages.size() - 1);
    }

    public synchronized void receiveMessage(String message) {
        messages.add(message);
        notify();
    }
}
