package gmbh.norisknofun.game;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.omg.CORBA.StringHolder;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import gmbh.norisknofun.game.networkmessages.ChangeState;
import gmbh.norisknofun.game.networkmessages.Dice;
import gmbh.norisknofun.game.networkmessages.EndGame;
import gmbh.norisknofun.game.networkmessages.NextPlayer;
import gmbh.norisknofun.game.statemachine.server.WaitingForPlayersState;
import gmbh.norisknofun.network.socket.SocketSelector;
import gmbh.norisknofun.network.socket.TCPClientSocket;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * Created by Philipp Mödritscher on 22.05.2017.
 */

public class NetworkMessagesTests {

    public ByteArrayOutputStream baos;
    ObjectOutputStream oos;
    @Before
    public void setUp() throws IOException {
         baos = new ByteArrayOutputStream ();
         oos = new ObjectOutputStream (baos);
    }
    @Test
    public void nextPlayerFields() {

        String playername="Player1";
        NextPlayer nextPlayer = new NextPlayer(playername);


       try{
            oos.writeObject (nextPlayer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        try (ObjectInputStream ois = new ObjectInputStream (bais)) {
            NextPlayer nextPlayer1 =(NextPlayer) ois.readObject();
            assertEquals(nextPlayer1.playername,playername);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    @Test
    public void nextPlayerTypes() {

        String playername="Player1";
        NextPlayer nextPlayer = new NextPlayer(playername);

        try{
            oos.writeObject (nextPlayer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        try (ObjectInputStream ois = new ObjectInputStream (bais)) {
            NextPlayer nextPlayer1 =(NextPlayer) ois.readObject();
            assertEquals(nextPlayer1.getType(),NextPlayer.class);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    @Test
    public void ChangeState() {



        WaitingForPlayersState stateMock = mock(WaitingForPlayersState.class);
        ChangeState changeState = new ChangeState(stateMock);

        try{
            oos.writeObject (changeState);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        try (ObjectInputStream ois = new ObjectInputStream (bais)) {
            ChangeState changeState1 =(ChangeState) ois.readObject();
            assertThat(changeState1.state,  instanceOf(WaitingForPlayersState.class));

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    @Test
    public void dice() {




        Dice dice = new Dice("Player 1",10,6);

        try{
           oos.writeObject (dice);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        try (ObjectInputStream ois = new ObjectInputStream (bais)) {
            Dice dice1 =(Dice) ois.readObject();
            assertEquals(dice1.playername,  "Player 1");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    @Test
    public void endGame() {




        EndGame endGame = new EndGame("Player 1",true);

        try{
            oos.writeObject (endGame);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        try (ObjectInputStream ois = new ObjectInputStream (bais)) {
            EndGame endGame1 =(EndGame) ois.readObject();
            assertEquals(endGame1.winner,  "Player 1");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }



}
