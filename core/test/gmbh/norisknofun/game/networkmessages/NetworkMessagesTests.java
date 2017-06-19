package gmbh.norisknofun.game.networkmessages;


import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import gmbh.norisknofun.game.networkmessages.common.NextPlayer;

import static org.junit.Assert.assertEquals;

/**
 * Created by Philipp Mödritscher on 22.05.2017.
 *
 */

public class NetworkMessagesTests {

    private ByteArrayOutputStream baos;
    private ObjectOutputStream oos;
    final String PLAYER = "Player1";
    final int DICE_RESULT = 6;
    final int TROOPS = 2;

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
        assertEquals(nextPlayer1.getPlayername(),PLAYER);

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
        assertEquals(dice1.getPlayername(),  PLAYER);

    }

    @Test
    public void diceGetterSetter() throws IOException, ClassNotFoundException {

        Dice dice = new Dice(PLAYER,10,6);

        dice.setPlayername(PLAYER);
        dice.setAmountofTroops(TROOPS);
        dice.setDiceResult(DICE_RESULT);
        assertEquals(dice.getPlayername(),  PLAYER);
        assertEquals(dice.getAmountofTroops(),  TROOPS);
        assertEquals(dice.getDiceResult(),  DICE_RESULT);

    }
    @Test
    public void endGameetterSetter() throws IOException, ClassNotFoundException {

        EndGame endGame = new EndGame(PLAYER,true);

        endGame.setGameend(true);
        endGame.setWinner(PLAYER);
        assertEquals(endGame.getWinner(),  PLAYER);
        assertEquals(endGame.isGameend(),  true);


    }
    @Test
    public void endGame() throws IOException, ClassNotFoundException{

        EndGame endGame = new EndGame(PLAYER,true);
        oos.writeObject (endGame);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        EndGame endGame1 =(EndGame) ois.readObject();
        assertEquals(endGame1.getWinner(), PLAYER);

    }



}