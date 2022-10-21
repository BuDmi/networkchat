package server;

import org.example.server.Chat;
import org.example.server.Client;
import org.example.server.ClientNotifier;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClientNotifierTest {
    private static Chat chat;
    private static ClientNotifier clientNotifier;

    @BeforeAll
    static void init() {
        chat = Mockito.mock(Chat.class);
        clientNotifier = new ClientNotifier(chat);
        clientNotifier.addNewClient(new Socket());
        clientNotifier.addNewClient(new Socket());
    }

    @Test
    public void checkThatClientNameIsExisted() {
        Client existedClient = Mockito.mock(Client.class);
        Mockito.when(existedClient.getClientName()).thenReturn("");
        assertTrue(clientNotifier.checkForExistedClientName(existedClient));
    }

    @Test
    public void checkThatClientNameIsNew() {
        Client newClient = Mockito.mock(Client.class);
        Mockito.when(newClient.getClientName()).thenReturn("New");
        assertFalse(clientNotifier.checkForExistedClientName(newClient));
    }

    @Test
    public void testLogMessage() {
        String expectedMessage = "Test message";
        Client newClient = Mockito.mock(Client.class);
        clientNotifier.notifyClients(expectedMessage, newClient);
        Mockito.verify(chat, Mockito.times(1)).logMessage(expectedMessage);
    }
}
