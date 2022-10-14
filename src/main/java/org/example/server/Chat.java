package org.example.server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Chat {
    private List<String> messages = new ArrayList<>();
    private AtomicInteger clientsNum = new AtomicInteger(0);

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

    public synchronized void incrementClientsNum() {
        clientsNum.incrementAndGet();
    }

    public synchronized void decrementClientsNum() {
        clientsNum.decrementAndGet();
    }
}
