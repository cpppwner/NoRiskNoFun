package gmbh.norisknofun.game.gamemessages.client;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by MödritscherPhilipp on 19.06.2017.
 */

public class ClientTest {

    private final String REASON = "REASON";

    @Test
    public void ClientConnected() {

        ClientConnected clientConnected = new ClientConnected();
        assertEquals(clientConnected.getType(),ClientConnected.class);
    }
    @Test
    public void ClientConnectionRefused() {

        ClientConnectionRefused clientConnectionRefused = new ClientConnectionRefused(REASON);
        assertEquals(clientConnectionRefused.getType(),ClientConnectionRefused.class);
        assertEquals(clientConnectionRefused.getReason(),REASON);
    }
    @Test
    public void ClientDisconnected() {

        ClientDisconnected clientDisconnected = new ClientDisconnected();
        assertEquals(clientDisconnected.getType(),ClientDisconnected.class);
    }
    @Test
    public void DisconnectClient() {

        DisconnectClient disconnectClient = new DisconnectClient(true);
        assertEquals(disconnectClient.getClass(),DisconnectClient.class);
        assertEquals(disconnectClient.isTerminateClient(),true);
    }
}
