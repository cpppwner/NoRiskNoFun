package gmbh.norisknofun.game.networkmessages.common;

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

public class CommonTests {

    private ByteArrayOutputStream baos;
    private ObjectOutputStream oos;
    private final String REGIONNAME = "Reg1";
    private  final String PLAYER = "Player1";

    @Before
    public void setUp() throws IOException {
        baos = new ByteArrayOutputStream ();
        oos = new ObjectOutputStream (baos);
    }
    @Test
    public void moveTroop() throws IOException, ClassNotFoundException {

        MoveTroop moveTroop = new MoveTroop(PLAYER, 10,"dest", "origin", -1);
        oos.writeObject (moveTroop);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        MoveTroop moveTroop1 =(MoveTroop) ois.readObject();
        assertEquals(moveTroop1.getPlayername(),PLAYER);

    }
    @Test
    public void moveTroopCheck() throws IOException, ClassNotFoundException {

        MoveTroopCheck moveTroopCheck = new MoveTroopCheck(PLAYER,true);
        oos.writeObject (moveTroopCheck);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        MoveTroopCheck moveTroopCheck1 =(MoveTroopCheck) ois.readObject();
        assertEquals(moveTroopCheck1.getPlayername(),PLAYER);

    }
    @Test
    public void moveTroopsDone() throws IOException, ClassNotFoundException{

        MoveTroopsDone moveTroopDone = new MoveTroopsDone(PLAYER);
        oos.writeObject(moveTroopDone);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        MoveTroopsDone moveTroopDone1 =(MoveTroopsDone) ois.readObject();
        assertEquals(moveTroopDone1.getPlayername(),  PLAYER);

    }
    @Test
    public void nextPlayer() throws IOException, ClassNotFoundException{

        NextPlayer nextPlayer = new NextPlayer(PLAYER);
        oos.writeObject (nextPlayer);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        NextPlayer nextPlayer1 =(NextPlayer) ois.readObject();
        assertEquals(nextPlayer1.getPlayername(), PLAYER);

    }
    @Test
    public void spawnTroop() throws IOException, ClassNotFoundException{

        SpawnTroop spawnTroop = new SpawnTroop(PLAYER,REGIONNAME);
        oos.writeObject (spawnTroop);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        SpawnTroop spawnTroop1 =(SpawnTroop) ois.readObject();
        assertEquals(spawnTroop1.getPlayername(),  PLAYER);

    }
    @Test
    public void spawnTroopCheck() throws IOException, ClassNotFoundException{

        SpawnTroopCheck spawnTroopCheck = new SpawnTroopCheck(false,"ERROR");
        oos.writeObject (spawnTroopCheck);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        SpawnTroopCheck spawnTroopCheck1 =(SpawnTroopCheck) ois.readObject();
        assertEquals(spawnTroopCheck1.isCanspawn(), false);

    }
}
