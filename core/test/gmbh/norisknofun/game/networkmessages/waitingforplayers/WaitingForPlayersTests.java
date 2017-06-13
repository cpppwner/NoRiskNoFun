package gmbh.norisknofun.game.networkmessages.waitingforplayers;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Philipp Mödritscher on 23.05.2017.
 *
 */

public class WaitingForPlayersTests {
    private ByteArrayOutputStream baos;
    private ObjectOutputStream oos;


    private  final String PLAYER = "Player1";
    private  final String MAP = "map1";
    private  final int MAXPLAYER = 4;
    private  final int RGB  = 255;
    private final String reason="Player with same Name not allowed";

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



        PlayerAccepted playerJoinedCheck = new PlayerAccepted(PLAYER);
        playerJoinedCheck.setMapName(MAP);
        playerJoinedCheck.setMaxNumPlayers(MAXPLAYER);
        playerJoinedCheck.setPlayerColor(RGB);

        oos.writeObject (playerJoinedCheck);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        PlayerAccepted playerJoinedCheck1 =(PlayerAccepted) ois.readObject();
        assertEquals(playerJoinedCheck1.getPlayerName(),PLAYER);
        assertEquals(playerJoinedCheck1.getMapName(),MAP);
        assertEquals(playerJoinedCheck1.getMaxNumPlayers(),MAXPLAYER);
        assertEquals(playerJoinedCheck1.getPlayerColor(),RGB);

    }

    @Test
    public void playerRejected() throws IOException, ClassNotFoundException {

        PlayerRejected playerRejected = new PlayerRejected();
        oos.writeObject (playerRejected);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        PlayerRejected playerRejected1 =(PlayerRejected) ois.readObject();
        assertEquals(playerRejected1.getReason(),reason);

    }

    @Test
    public void playersInGame() throws IOException, ClassNotFoundException {

        PlayersInGame playersInGame = new PlayersInGame();
        oos.writeObject (playersInGame);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        PlayersInGame playersInGame1 =(PlayersInGame) ois.readObject();
        assertNull(playersInGame1.getAllPlayers());

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
