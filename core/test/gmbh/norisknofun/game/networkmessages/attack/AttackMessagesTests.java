package gmbh.norisknofun.game.networkmessages.attack;


import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import gmbh.norisknofun.game.networkmessages.attack.choosetroops.ChooseTroopsAmount;
import gmbh.norisknofun.game.networkmessages.attack.choosetroops.ChooseTroopsAmountCheck;
import gmbh.norisknofun.game.networkmessages.attack.evaluatedice.AttackResult;
import gmbh.norisknofun.game.networkmessages.attack.evaluatedice.DiceAmount;
import gmbh.norisknofun.game.networkmessages.attack.evaluatedice.DiceResult;
import gmbh.norisknofun.game.networkmessages.attack.loser.ContinueAttack;
import gmbh.norisknofun.game.statemachine.server.WaitingForPlayersState;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by Philipp Mödritscher on 22.05.2017.
 *
 */

public class AttackMessagesTests {

    private ByteArrayOutputStream baos;
    private ObjectOutputStream oos;
    final String PLAYER = "Player1";

    @Before
    public void setUp() throws IOException {
        baos = new ByteArrayOutputStream ();
        oos = new ObjectOutputStream (baos);
    }
    @Test
    public void ChooseTroopsAmount() throws IOException, ClassNotFoundException {


        ChooseTroopsAmount chooseTroopsAmount = new ChooseTroopsAmount(6);
        oos.writeObject (chooseTroopsAmount);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        ChooseTroopsAmount chooseTroopsAmount1 =(ChooseTroopsAmount) ois.readObject();
        assertEquals(chooseTroopsAmount1.amount,6);




    }
    @Test
    public void ChooseTroopsAmountCheck() throws IOException, ClassNotFoundException {


        ChooseTroopsAmountCheck chooseTroopsAmountCheck = new ChooseTroopsAmountCheck(true);
        oos.writeObject (chooseTroopsAmountCheck);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        ChooseTroopsAmountCheck chooseTroopsAmountCheck1 =(ChooseTroopsAmountCheck) ois.readObject();
        assertEquals(chooseTroopsAmountCheck1.check,true);


    }
    @Test
    public void AttackResult() throws IOException, ClassNotFoundException {

        AttackResult attackResult = new AttackResult(PLAYER);
        oos.writeObject (attackResult);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        AttackResult attackResult1 =(AttackResult) ois.readObject();
        assertEquals(attackResult1.playername,  PLAYER);

    }
    @Test
    public void DiceAmount() throws IOException, ClassNotFoundException {

        DiceAmount diceAmount = new DiceAmount(10);
        oos.writeObject (diceAmount);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        DiceAmount diceAmount1 =(DiceAmount) ois.readObject();
        assertEquals(diceAmount1.amount,  10);




    }
    @Test
    public void DiceResult() throws IOException, ClassNotFoundException {



        int[] arr = {10,1,1};
        DiceResult diceResult = new DiceResult(arr);
        oos.writeObject (diceResult);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        DiceResult diceResult1 =(DiceResult) ois.readObject();
        assertEquals(diceResult1.diceResults[0],  10);



    }
    @Test
    public void ContinueAttack() throws IOException, ClassNotFoundException {

        ContinueAttack continueAttack = new ContinueAttack(true);
        oos.writeObject (continueAttack);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        ContinueAttack continueAttack1 =(ContinueAttack) ois.readObject();
        assertEquals(continueAttack1.decision, true);




    }



}
