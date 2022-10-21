package server;

import org.example.logger.Logger;
import org.example.server.Chat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChatTest {
    private static Chat chat;

    @BeforeAll
    static void init() {
        Logger logger = Mockito.mock(Logger.class);
        chat = new Chat(logger);
    }

    @Test
    public void sendMessage() {
        String expected = "test message";
        chat.receiveMessage("test message");
        String actual = chat.sendMessage();
        assertEquals(expected, actual);
    }

    @Test
    public void threadIsWaitingMessage() throws InterruptedException {
        Thread testThread = new Thread(() -> chat.sendMessage());
        testThread.start();
        Thread.sleep(1000);
        assertEquals(Thread.State.WAITING, testThread.getState());
        chat.receiveMessage("test message");
        Thread.sleep(1000);
        assertEquals(Thread.State.TERMINATED, testThread.getState());
    }
}
