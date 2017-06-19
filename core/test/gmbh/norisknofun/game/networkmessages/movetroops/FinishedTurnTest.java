package gmbh.norisknofun.game.networkmessages.movetroops;

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

public class FinishedTurnTest {

    private ByteArrayOutputStream baos;
    private ObjectOutputStream oos;
    private  final String PLAYER = "Player1";


    @Before
    public void setUp() throws IOException {
        baos = new ByteArrayOutputStream ();
        oos = new ObjectOutputStream (baos);
    }
    @Test
    public void FinishTurn() throws IOException, ClassNotFoundException {

        FinishTurn finishTurn = new FinishTurn("");
        oos.writeObject (finishTurn);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        FinishTurn finishTurn1 =(FinishTurn) ois.readObject();
        assertEquals(finishTurn1.getType(),FinishTurn.class);

    }
    @Test
    public void FinishTurnGetterSetter() throws IOException, ClassNotFoundException {

        FinishTurn finishTurn = new FinishTurn(PLAYER);
        finishTurn.setPlayerName(PLAYER);
        assertEquals(finishTurn.getPlayerName(),PLAYER);

    }
}
