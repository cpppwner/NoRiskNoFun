package gmbh.norisknofun.game.networkmessages.spread;

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

public class SpreadTests {

    private ByteArrayOutputStream baos;
    private ObjectOutputStream oos;


    private  final String PLAYER = "Player1";

    @Before
    public void setUp() throws IOException {
        baos = new ByteArrayOutputStream ();
        oos = new ObjectOutputStream (baos);
    }
    @Test
    public void playerSpread() throws IOException, ClassNotFoundException {

        PlayerSpread playerSpread = new PlayerSpread(PLAYER,true);
        oos.writeObject (playerSpread);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        PlayerSpread playerSpread1 =(PlayerSpread) ois.readObject();
        assertEquals(playerSpread1.getPlayername(),PLAYER);

    }
    @Test
    public void playerSpreadCheck() throws IOException, ClassNotFoundException {

        PlayerSpreadCheck playerSpreadCheck = new PlayerSpreadCheck(PLAYER,true);
        oos.writeObject (playerSpreadCheck);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        PlayerSpreadCheck playerSpreadCheck1 =(PlayerSpreadCheck) ois.readObject();
        assertEquals(playerSpreadCheck1.getPlayername(),PLAYER);

    }
    @Test
    public void playerSpreadFinished() throws IOException, ClassNotFoundException {

        PlayerSpreadFinished playerSpreadFinished = new PlayerSpreadFinished(PLAYER);
        oos.writeObject (playerSpreadFinished);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        PlayerSpreadFinished playerSpreadFinished1 =(PlayerSpreadFinished) ois.readObject();
        assertEquals(playerSpreadFinished1.getPlayername(),PLAYER);

    }
}
