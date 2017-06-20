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
    private final int AMOUNT = 1;
    private  final String PLAYER = "Player1";
    private  final String ERRORMESSAGE = "ERROR";
    private  final int ID=10;

    @Before
    public void setUp() throws IOException {
        baos = new ByteArrayOutputStream ();
        oos = new ObjectOutputStream (baos);
    }
    @Test
    public void moveTroop() throws IOException, ClassNotFoundException {

        MoveTroop moveTroop = new MoveTroop("dest", "origin", -1);
        oos.writeObject (moveTroop);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        MoveTroop moveTroop1 =(MoveTroop) ois.readObject();
        assertEquals(moveTroop1.getFromRegion(),"dest");
        assertEquals(moveTroop1.getToRegion(),"origin");
        assertEquals(moveTroop1.getFigureId(),-1);

    }
    @Test
    public void moveTroopGetterSetter() throws IOException, ClassNotFoundException {

        MoveTroop moveTroop = new MoveTroop();
        moveTroop.setFigureId(-2);
        moveTroop.setFromRegion("dest2");
        moveTroop.setToRegion("origin2");
        assertEquals(moveTroop.getFromRegion(),"dest2");
        assertEquals(moveTroop.getToRegion(),"origin2");
        assertEquals(moveTroop.getFigureId(),-2);

    }
    @Test
    public void moveTroopCheck() throws IOException, ClassNotFoundException {

        MoveTroopCheck moveTroopCheck = new MoveTroopCheck(true,ERRORMESSAGE);
        oos.writeObject (moveTroopCheck);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        MoveTroopCheck moveTroopCheck1 =(MoveTroopCheck) ois.readObject();
        assertEquals(moveTroopCheck1.getErrorMessage(),ERRORMESSAGE);
        assertEquals(moveTroopCheck.isMovePossible(),true);

    }
    @Test
    public void moveTroopCheckGetterSetter() throws IOException, ClassNotFoundException {

        MoveTroopCheck moveTroopCheck = new MoveTroopCheck(true,ERRORMESSAGE);
        moveTroopCheck.setErrorMessage(ERRORMESSAGE);
        moveTroopCheck.setMovePossible(true);
        assertEquals(moveTroopCheck.getErrorMessage(),ERRORMESSAGE);
        assertEquals(moveTroopCheck.isMovePossible(),true);

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
    public void moveTroopsDoneGetterSetter() throws IOException, ClassNotFoundException {

        MoveTroopsDone moveTroopDone = new MoveTroopsDone(PLAYER);
        moveTroopDone.setPlayername(PLAYER);
        assertEquals(moveTroopDone.getPlayername(),PLAYER);


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
    public void nextPlayerGetterSetter() throws IOException, ClassNotFoundException {

        NextPlayer nextPlayer = new NextPlayer(PLAYER);
        nextPlayer.setPlayername(PLAYER);
        assertEquals(nextPlayer.getPlayername(),PLAYER);


    }
    @Test
    public void spawnTroop() throws IOException, ClassNotFoundException{

        SpawnTroop spawnTroop = new SpawnTroop(REGIONNAME);
        spawnTroop.setPlayername(PLAYER);
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
    @Test
    public void removeTroopGetterSetter() throws IOException, ClassNotFoundException {

        RemoveTroop removeTroop = new RemoveTroop();
        removeTroop.setAmount(AMOUNT);
        removeTroop.setRegion(REGIONNAME);
        assertEquals(removeTroop.getAmount(),AMOUNT);
        assertEquals(removeTroop.getRegion(),REGIONNAME);
    }
    @Test
    public void spwanTroopGetterSetter() throws IOException, ClassNotFoundException {

        SpawnTroop spawnTroop = new SpawnTroop(REGIONNAME,PLAYER);
        spawnTroop.setRegionname(REGIONNAME);
        spawnTroop.setId(ID);
        assertEquals(spawnTroop.getId(),ID);
        assertEquals(spawnTroop.getRegionname(),REGIONNAME);
    }
    @Test
    public void spawnTroopCheckGetterSetter() throws IOException, ClassNotFoundException {

        SpawnTroopCheck spawnTroopCheck = new SpawnTroopCheck(false,"ERROR");
        spawnTroopCheck.setCanspawn(true);
        spawnTroopCheck.setErrormessage(ERRORMESSAGE);
        assertEquals(spawnTroopCheck.isCanspawn(),true);
        assertEquals(spawnTroopCheck.getErrormessage(),ERRORMESSAGE);
    }
}
