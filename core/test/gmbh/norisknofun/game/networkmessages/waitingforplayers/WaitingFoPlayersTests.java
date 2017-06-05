package gmbh.norisknofun.game.networkmessages.waitingforplayers;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


import static org.junit.Assert.assertEquals;

/**
 * Created by Philipp Mödritscher on 23.05.2017.
 *
 */

public class WaitingFoPlayersTests {
    private ByteArrayOutputStream baos;
    private ObjectOutputStream oos;


    private  final String PLAYER = "Player1";
    private  final boolean START = true;
    @Before
    public void setUp() throws IOException {
        baos = new ByteArrayOutputStream ();
        oos = new ObjectOutputStream (baos);
    }
    @Test
    public void playerJoined() throws IOException, ClassNotFoundException {

        PlayerJoined playerJoined = new PlayerJoined(PLAYER);
        oos.writeObject (playerJoined);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        PlayerJoined playerJoined1 =(PlayerJoined) ois.readObject();
        assertEquals(playerJoined1.getPlayerName(),PLAYER);

    }
    @Test
    public void playerJoinedCheck() throws IOException, ClassNotFoundException {

        PlayerJoinedCheck playerJoinedCheck = new PlayerJoinedCheck(PLAYER);
        oos.writeObject (playerJoinedCheck);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        PlayerJoinedCheck playerJoinedCheck1 =(PlayerJoinedCheck) ois.readObject();
        assertEquals(playerJoinedCheck1.getPlayerName(),PLAYER);

    }
    @Test
    public void startGame() throws IOException, ClassNotFoundException {

        StartGame startGame = new StartGame(START);
        oos.writeObject (startGame);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        StartGame startGame1 =(StartGame) ois.readObject();
        assertEquals(startGame1.isStartGame(),START);

    }



}
