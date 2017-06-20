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
import gmbh.norisknofun.game.networkmessages.attack.evaluatedice.IsAttacked;
import gmbh.norisknofun.game.networkmessages.attack.loser.ContinueAttack;

import static org.junit.Assert.assertEquals;

/**
 * Created by Philipp Mödritscher on 22.05.2017.
 *
 */

public class AttackMessagesTests {

    private ByteArrayOutputStream baos;
    private ObjectOutputStream oos;
    private  final String ATTACKEREGION = "region1";
    private final String DEFENDERREGION = "region2";
    private final String DEFENDERREGIONOWNER = "owner2";
    private  final String ERRORMESSAGE = "ERROR";
    private  final String ID="12345";


    @Before
    public void setUp() throws IOException {
        baos = new ByteArrayOutputStream ();
        oos = new ObjectOutputStream (baos);
    }
    @Test
    public void ChooseTroopsAmount() throws IOException, ClassNotFoundException {


        ChooseTroopsAmount chooseTroopsAmount = new ChooseTroopsAmount(6);
        chooseTroopsAmount.setAmount(6);
        oos.writeObject (chooseTroopsAmount);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        ChooseTroopsAmount chooseTroopsAmount1 =(ChooseTroopsAmount) ois.readObject();
        assertEquals(chooseTroopsAmount1.getAmount(),6);




    }
    @Test
    public void ChooseTroopsAmountCheck() throws IOException, ClassNotFoundException {


        ChooseTroopsAmountCheck chooseTroopsAmountCheck = new ChooseTroopsAmountCheck(true,"");

        oos.writeObject (chooseTroopsAmountCheck);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        ChooseTroopsAmountCheck chooseTroopsAmountCheck1 =(ChooseTroopsAmountCheck) ois.readObject();
        assertEquals(chooseTroopsAmountCheck1.isCheck(),true);


    }
    @Test
    public void AttackResult() throws IOException, ClassNotFoundException {

        AttackResult attackResult = new AttackResult();
        attackResult.setWon(true);
        attackResult.setDefenderRegion(DEFENDERREGION);
        attackResult.setDefenderRegionOwner(DEFENDERREGIONOWNER);
        attackResult.setDefenderTroops(2);
        attackResult.setAttackerTroops(1);
        attackResult.setAttackerRegion(ATTACKEREGION);

        oos.writeObject (attackResult);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        AttackResult attackResult1 =(AttackResult) ois.readObject();
        assertEquals(attackResult1.getAttackerRegion(),  ATTACKEREGION);
        assertEquals(attackResult1.getAttackerTroops(),  1);
        assertEquals(attackResult1.getDefenderRegion(),  DEFENDERREGION);
        assertEquals(attackResult1.getDefenderRegionOwner(),  DEFENDERREGIONOWNER);
        assertEquals(attackResult1.getDefenderTroops(),  2);
        assertEquals(attackResult.isWon(),true);

    }
    @Test
    public void DiceAmount() throws IOException, ClassNotFoundException {

        DiceAmount diceAmount = new DiceAmount(10);
        diceAmount.setAmount(10);
        oos.writeObject (diceAmount);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        DiceAmount diceAmount1 =(DiceAmount) ois.readObject();
        assertEquals(diceAmount1.getAmount(),  10);

    }
    @Test
    public void DiceResult() throws IOException, ClassNotFoundException {

        int[] arr = {10,1,1};
        DiceResult diceResult = new DiceResult(arr);
        diceResult.setDiceResults(arr);
        oos.writeObject (diceResult);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        DiceResult diceResult1 =(DiceResult) ois.readObject();
        assertEquals(diceResult1.getDiceResults()[0],  10);

    }
    @Test
    public void ContinueAttack() throws IOException, ClassNotFoundException {

        ContinueAttack continueAttack = new ContinueAttack(true);
        oos.writeObject (continueAttack);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        ContinueAttack continueAttack1 =(ContinueAttack) ois.readObject();
        assertEquals(continueAttack1.isDecision(), true);

    }

    @Test
    public void ContinueAttackGetterSetter() throws IOException, ClassNotFoundException {

        ContinueAttack continueAttack = new ContinueAttack(true);
        continueAttack.setDecision(true);
        assertEquals(continueAttack.isDecision(), true);

    }
    @Test
    public void isAttackedGetterSetter() throws IOException, ClassNotFoundException {

        IsAttacked isAttacked = new IsAttacked();

    }
    @Test
    public void AttackResultGetterSetter() throws IOException, ClassNotFoundException {

        AttackResult attackResult = new AttackResult();
        attackResult.setWinnerId(ID);
        attackResult.setLoserId(ID);
        assertEquals(attackResult.getWinnerId(), ID);
        assertEquals(attackResult.getLoserId(), ID);

    }
    @Test
    public void ChooseTroopsAmountCheckGetterSetter() throws IOException, ClassNotFoundException {

        ChooseTroopsAmountCheck chooseTroopsAmountCheck = new ChooseTroopsAmountCheck(true,"");
        chooseTroopsAmountCheck.setCheck(true);
        chooseTroopsAmountCheck.setErrormessage(ERRORMESSAGE);
        assertEquals(chooseTroopsAmountCheck.isCheck(), true);
        assertEquals(chooseTroopsAmountCheck.getErrormessage(), ERRORMESSAGE);

    }




}
