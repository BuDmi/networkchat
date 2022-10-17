package org.example.server;

import org.example.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    private final List<String> messages = new ArrayList<>();
    private final Logger logger;

    public Chat(Logger logger) {
        this.logger = logger;
    }

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

    public void logMessage(String message) {
        logger.logNewMessage(message);
    }
}
