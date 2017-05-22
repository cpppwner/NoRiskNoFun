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
import gmbh.norisknofun.game.networkmessages.NextPlayer;
import gmbh.norisknofun.game.statemachine.server.WaitingForPlayersState;
import gmbh.norisknofun.network.socket.SocketSelector;
import gmbh.norisknofun.network.socket.TCPClientSocket;

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
        NextPlayer nextPlayer = new NextPlayer();
        nextPlayer.playername=playername;

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
        NextPlayer nextPlayer = new NextPlayer();
        nextPlayer.playername= playername;
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

        String playername="Player1";

        WaitingForPlayersState stateMock = mock(WaitingForPlayersState.class);
        ChangeState changeState = new ChangeState();
        changeState.state = stateMock;
        try{
            oos.writeObject (changeState);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        try (ObjectInputStream ois = new ObjectInputStream (bais)) {
            ChangeState changeState1 =(ChangeState) ois.readObject();
            assertEquals(changeState1.state.getClass(),WaitingForPlayersState.class);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
