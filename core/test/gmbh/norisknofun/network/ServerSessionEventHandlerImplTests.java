package gmbh.norisknofun.network;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerJoined;
import gmbh.norisknofun.game.statemachine.server.ServerContext;
import gmbh.norisknofun.network.socket.SocketFactory;
import gmbh.norisknofun.network.socket.SocketFactoryImpl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by Philipp Mödritscher on 31.05.2017.
 */

public class ServerSessionEventHandlerImplTests {
    private ByteArrayOutputStream baos;
    private ObjectOutputStream oos;


    private  final String PLAYER = "Player1";
    private  final boolean START = true;

    private NetworkServer networkServer;
    public ServerSessionEventHandlerImpl serverSessionEventHandler;

    @Before
    public void setUp() throws IOException {
        baos = new ByteArrayOutputStream ();
        oos = new ObjectOutputStream (baos);
        SocketFactory socketFactory = new SocketFactoryImpl();
        serverSessionEventHandler = new ServerSessionEventHandlerImpl(mock(ServerContext.class));
        this.networkServer = new NetworkServer(socketFactory, serverSessionEventHandler);

    }
    @Test
    public void convertByteArraytoMessageTest() throws IOException,ClassNotFoundException {
        PlayerJoined playerJoined = new PlayerJoined(PLAYER);
        oos.writeObject (playerJoined);
        PlayerJoined playerJoined1 = (PlayerJoined) ServerSessionEventHandlerImpl.convertByteArraytoMessage(baos.toByteArray());
        assertEquals(playerJoined1.playername,PLAYER);
    }



}
