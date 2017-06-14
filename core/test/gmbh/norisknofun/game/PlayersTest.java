package gmbh.norisknofun.game;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Created by Katharina on 06.06.17.
 */

public class PlayersTest {

    Players players;
    Player player1;
    Player player2;

    @Before
    public void initialize() {
        players = new Players();
        player1 = new Player("P1", "123");
        player2 = new Player("P2", "456");
    }

    @Test
    public void getPlayersCorrectlyReturnsList() {
        List obtained = players.getPlayerlist();

        assertThat(obtained, instanceOf(List.class) );
    }


    @Test
    public void addPlayerAddsPlayerInEmptyList() {
        players.addPlayer(player1);

        List<Player> obtained = players.getPlayerlist();

        assertEquals(obtained.size(), 1);
        assertSame(player1, obtained.get(0));
    }

    @Test
    public void addPlayerAddsMultiplePlayers() {
        players.addPlayer(player1);
        players.addPlayer(player2);

        List<Player> obtained = players.getPlayerlist();

        assertEquals(obtained.size(), 2);
        assertSame(player1, obtained.get(0));
        assertSame(player2, obtained.get(1));
    }

    @Test
    public void addPlayerReturnsTrueOnSuccess() {
        boolean obtained = players.addPlayer(player1);

        assertEquals(true, obtained);
    }

    @Test
    public void addPlayerDoesntAddSamePlayerTwice() {
        players.addPlayer(player1);
        boolean obtained = players.addPlayer(player1);

        assertEquals(false, obtained);
        assertEquals(1, players.getPlayerlist().size());
    }

    @Test
    public void addPlayerDoesntAddSamePlayernameTwice() {
        Player player3 = new Player(player1.getPlayerName(), "987");

        players.addPlayer(player1);
        boolean obtained = players.addPlayer(player3);

        assertEquals(false, obtained);
        assertEquals(1, players.getPlayerlist().size());
    }

    @Test
    public void addPlayerDoesntAddSameIdTwice() {
        Player player3 = new Player("Someone", player1.getId());

        players.addPlayer(player1);
        boolean obtained = players.addPlayer(player3);

        assertEquals(false, obtained);
        assertEquals(1, players.getPlayerlist().size());
    }

    @Test
    public void addPlayerDoesntAddNullPlayername() {
        Player player3 = new Player(null, "123");

        boolean obtained = players.addPlayer(player3);

        assertEquals(false, obtained);
        assertEquals(0, players.getPlayerlist().size());
    }

    @Test
    public void addPlayerDoesntAddNullId() {
        Player player3 = new Player("Someone", null);

        boolean obtained = players.addPlayer(player3);

        assertEquals(false, obtained);
        assertEquals(0, players.getPlayerlist().size());
    }

    @Test
    public void removePlayerRemovesPlayerWithCorrectName() {
        players.addPlayer(player1);
        players.addPlayer(player2);

        boolean obtained = players.removePlayer(player1.getPlayerName());

        assertEquals(true, obtained);
        assertEquals(1, players.getPlayerlist().size());
        assertSame(player2, players.getPlayerlist().get(0));
    }

    @Test
    public void removePlayerRemovesLastPlayer() {
        players.addPlayer(player1);

        boolean obtained = players.removePlayer(player1.getPlayerName());

        assertEquals(true, obtained);
        assertEquals(0, players.getPlayerlist().size());
    }

    @Test
    public void removePlayerDoesntRemoveUnknownPlayer() {
        players.addPlayer(player1);

        boolean obtained = players.removePlayer("Unknown");

        assertEquals(false, obtained);
        assertEquals(1, players.getPlayerlist().size());
        assertSame(player1, players.getPlayerlist().get(0));
    }

    @Test
    public void removePlayerDoesntRemoveNull() {
        players.addPlayer(player1);
        players.addPlayer(player2);

        players.removePlayer(player1.getPlayerName());
        boolean obtained = players.removePlayer(player1.getPlayerName());

        assertEquals(false, obtained);
        assertEquals(1, players.getPlayerlist().size());
        assertSame(player2, players.getPlayerlist().get(0));
    }

    @Test
    public void removePlayerDoesntRemoveSamePlayerTwice() {
        players.addPlayer(player1);

        boolean obtained = players.removePlayer(null);

        assertEquals(false, obtained);
        assertEquals(1, players.getPlayerlist().size());
        assertSame(player1, players.getPlayerlist().get(0));
    }

    @Test
    public void removePlayerDoesntRemoveOnEmptyList() {
        boolean obtained = players.removePlayer("Someone");

        assertEquals(false, obtained);
        assertEquals(0, players.getPlayerlist().size());
    }


    @Test
    public void getPlayerByIDReturnsCorrectPlayer() {
        players.addPlayer(player1);
        players.addPlayer(player2);

        Player obtained = players.getPlayerByID("123");

        assertSame(player1, obtained);
    }

    @Test
    public void getPlayerByIDReturnsNullOnUnknownName() {
        players.addPlayer(player1);

        Player obtained = players.getPlayerByID("000");

        assertNull(obtained);
    }

    @Test
    public void getPlayerByIDWorksOnEmptyList() {
        Player obtained = players.getPlayerByID("000");

        assertNull(obtained);
    }

    @Test
    public void getPlayerByIDWorksOnNullName() {
        players.addPlayer(player1);

        Player obtained = players.getPlayerByID(null);

        assertNull(obtained);
    }

    @Test
    public void getNextPlayernameReturnsCorrectName() {
        players.addPlayer(player1);
        players.addPlayer(player2);

        String obtained = players.getNextPlayername(player1.getPlayerName());

        assertEquals(player2.getPlayerName(), obtained);
    }

    @Test
    public void getNextPlayernameResetsIndexCorrectly() {
        players.addPlayer(player1);
        players.addPlayer(player2);

        String obtained = players.getNextPlayername(player2.getPlayerName());

        assertEquals(player1.getPlayerName(), obtained);
    }

    @Test
    public void getNextPlayernameWorksWithSinglePlayer() {
        players.addPlayer(player1);

        String obtained = players.getNextPlayername(player1.getPlayerName());

        assertEquals(player1.getPlayerName(), obtained);
    }

    @Test
    public void getNextPlayernameReturnsNullWithUnknownName() {
        players.addPlayer(player1);

        String obtained = players.getNextPlayername(null);

        assertNull(obtained);
    }

    @Test
    public void getNextPlayernameReturnsNullOnEmptyList() {

        String obtained = players.getNextPlayername("ABC");

        assertNull(obtained);
    }
}
