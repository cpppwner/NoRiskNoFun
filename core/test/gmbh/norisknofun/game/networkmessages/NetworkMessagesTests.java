package gmbh.norisknofun.game.networkmessages;


import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import gmbh.norisknofun.game.networkmessages.common.NextPlayer;
import gmbh.norisknofun.game.statemachine.server.WaitingForPlayersState;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by Philipp Mödritscher on 22.05.2017.
 *
 */

public class NetworkMessagesTests {

    private ByteArrayOutputStream baos;
    private ObjectOutputStream oos;
    final String PLAYER = "Player1";

    @Before
    public void setUp() throws IOException {
         baos = new ByteArrayOutputStream ();
         oos = new ObjectOutputStream (baos);
    }
    @Test
    public void nextPlayerFields() throws IOException, ClassNotFoundException {


        NextPlayer nextPlayer = new NextPlayer(PLAYER);
        oos.writeObject (nextPlayer);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        NextPlayer nextPlayer1 =(NextPlayer) ois.readObject();
        assertEquals(nextPlayer1.playername,PLAYER);

    }
    @Test
    public void nextPlayerTypes() throws IOException, ClassNotFoundException {

        NextPlayer nextPlayer = new NextPlayer(PLAYER);
        oos.writeObject (nextPlayer);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        NextPlayer nextPlayer1 =(NextPlayer) ois.readObject();
        assertEquals(nextPlayer1.getType(),NextPlayer.class);

    }

    @Test
    public void dice() throws IOException, ClassNotFoundException {

        Dice dice = new Dice(PLAYER,10,6);
        oos.writeObject (dice);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        Dice dice1 =(Dice) ois.readObject();
        assertEquals(dice1.playername,  PLAYER);

    }
    @Test
    public void endGame() throws IOException, ClassNotFoundException{

        EndGame endGame = new EndGame(PLAYER,true);
        oos.writeObject (endGame);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        EndGame endGame1 =(EndGame) ois.readObject();
        assertEquals(endGame1.winner, PLAYER);

    }



}
