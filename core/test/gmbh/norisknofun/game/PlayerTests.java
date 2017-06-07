package gmbh.norisknofun.game;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Katharina on 06.06.17.
 */

public class PlayerTests {
    Player player;

    @Before
    public void initialize() {
        player = new Player("P1", "123");
    }

    @Test
    public void playerWithNoArgumentHasNoNullFields() {
        Player player2 = new Player("P1", "123");

        assertNotNull(player2.getId());
        assertNotNull(player2.getPlayername());
    }

    @Test
    public void constructorProperlyInitializesValues() {
        Player player2 = new Player("P2", "456");


        assertEquals(player2.getId(), "456");
        assertEquals(player2.getPlayername(), "P2");
        assertEquals(player2.getTroopToSpread(), 0);
        assertEquals(player2.ishost(), false);
    }

    @Test
    public void getPlayernameReturnsCorrectName() {
        assertEquals(player.getPlayername(), "P1");
    }

    @Test
    public void getTroopToSpreadReturnsCorrectAmount() {
        assertEquals(player.getTroopToSpread(), 0);
    }

    @Test
    public void getIdReturnsCorrectId() {
        assertEquals(player.getId(), "123");
    }

    @Test
    public void isHostCorrectlyReturnsHost() {
        assertEquals(player.ishost(), false);
    }

    @Test
    public void setPlayernameSetsPlayername() {
        player.setPlayername("ABC");

        assertEquals(player.getPlayername(), "ABC");
    }

    @Test
    public void setIdSetsId() {
        player.setId("456");

        assertEquals(player.getId(), "456");
    }

    @Test
    public void setIshostCorrectlySetsHost() {
        player.setIshost(true);

        assertEquals(player.ishost(), true);
    }

    @Test
    public void setTroopToSpreadSetsCorrectAmount() {
        player.setTroopToSpread(2);

        assertEquals(player.getTroopToSpread(), 2);
    }
}
