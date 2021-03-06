package gmbh.norisknofun.game.networkmessages.choosetarget;


import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import static org.junit.Assert.assertEquals;

/**
 * Created by Philipp Mödritscher on 22.05.2017.
 *
 */

public class ChooseTargetTests {

    private ByteArrayOutputStream baos;
    private ObjectOutputStream oos;
    final String REGION1 = "Reg1";
    final String REGION2= "Reg2";
    private  final String ERRORMESSAGE = "ERROR";

    @Before
    public void setUp() throws IOException {
         baos = new ByteArrayOutputStream ();
         oos = new ObjectOutputStream (baos);
    }
    @Test
    public void attackRegion() throws IOException, ClassNotFoundException {

        AttackRegion attackRegion = new AttackRegion(REGION1,REGION2);
        oos.writeObject (attackRegion);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        AttackRegion attackRegion1 =(AttackRegion) ois.readObject();
        assertEquals(attackRegion1.getAttackerRegion(), REGION1);
        assertEquals(attackRegion.getDefenderRegion(),REGION2);

    }
    @Test
    public void attackRegionCheck() throws IOException, ClassNotFoundException {

        AttackRegionCheck attackRegionCheck = new AttackRegionCheck(true,"");
        oos.writeObject (attackRegionCheck);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        AttackRegionCheck attackRegionCheck1 =(AttackRegionCheck) ois.readObject();
        assertEquals(attackRegionCheck1.isAttackreachable(),true);

    }
    @Test
    public void noAttack() throws IOException, ClassNotFoundException{

        NoAttack noAttack = new NoAttack();
        oos.writeObject (noAttack);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        NoAttack noAttack1 =(NoAttack) ois.readObject();
        assertEquals(noAttack1.getType(),  NoAttack.class);

    }
    @Test
    public void attackRegionGetterSetter() throws IOException, ClassNotFoundException {

        AttackRegion attackRegion = new AttackRegion(REGION1,REGION2);
        attackRegion.setAttackerRegion(REGION1);
        attackRegion.setDefenderRegion(REGION2);
        assertEquals(attackRegion.getAttackerRegion(), REGION1);
        assertEquals(attackRegion.getDefenderRegion(),REGION2);

    }
    @Test
    public void attackRegionCheckGetterSetter() throws IOException, ClassNotFoundException {

        AttackRegionCheck attackRegionCheck = new AttackRegionCheck(true,"");
        attackRegionCheck.setAttackreachable(true);
        attackRegionCheck.setErrorMessage(ERRORMESSAGE);
        assertEquals(attackRegionCheck.isAttackreachable(), true);
        assertEquals(attackRegionCheck.getErrorMessage(),ERRORMESSAGE);

    }
    @Test
    public void noAttackGetterSetter() throws IOException, ClassNotFoundException {

        NoAttack noAttack = new NoAttack();


    }
}
