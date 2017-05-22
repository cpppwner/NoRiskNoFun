package gmbh.norisknofun.game.networkmessages.choosetarget;


import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import gmbh.norisknofun.game.networkmessages.ChangeState;
import gmbh.norisknofun.game.networkmessages.Dice;
import gmbh.norisknofun.game.networkmessages.EndGame;
import gmbh.norisknofun.game.networkmessages.NextPlayer;
import gmbh.norisknofun.game.statemachine.server.WaitingForPlayersState;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by Philipp Mödritscher on 22.05.2017.
 *
 */

public class ChooseTargetTests {

    private ByteArrayOutputStream baos;
    private ObjectOutputStream oos;
    final String REGIONNAME = "Reg1";

    @Before
    public void setUp() throws IOException {
         baos = new ByteArrayOutputStream ();
         oos = new ObjectOutputStream (baos);
    }
    @Test
    public void attackRegion() throws IOException, ClassNotFoundException {

        AttackRegion attackRegion = new AttackRegion(REGIONNAME);
        oos.writeObject (attackRegion);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        AttackRegion attackRegion1 =(AttackRegion) ois.readObject();
        assertEquals(attackRegion1.regionname,REGIONNAME);

    }
    @Test
    public void attackRegionCheck() throws IOException, ClassNotFoundException {

        AttackRegionCheck attackRegionCheck = new AttackRegionCheck(true);
        oos.writeObject (attackRegionCheck);
        ByteArrayInputStream bais = new ByteArrayInputStream (baos.toByteArray ());
        ObjectInputStream ois = new ObjectInputStream (bais);
        AttackRegionCheck attackRegionCheck1 =(AttackRegionCheck) ois.readObject();
        assertEquals(attackRegionCheck1.attackreachable,true);

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
}
