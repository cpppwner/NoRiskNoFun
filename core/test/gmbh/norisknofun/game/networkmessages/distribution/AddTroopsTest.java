package gmbh.norisknofun.game.networkmessages.distribution;

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

public class AddTroopsTest {

    private ByteArrayOutputStream baos;
    private ObjectOutputStream oos;

    private  final int AMOUNT = 10;

    @Before
    public void setUp() throws IOException {
        baos = new ByteArrayOutputStream ();
        oos = new ObjectOutputStream (baos);
    }
    @Test
    public void addTroops() throws IOException, ClassNotFoundException {

        AddTroops addTroops = new AddTroops(AMOUNT);
        oos.writeObject (addTroops);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        AddTroops addTroops1 =(AddTroops) ois.readObject();
        assertEquals(addTroops1.getAmount(),AMOUNT);

    }
}
