package gmbh.norisknofun.game;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;


/**
 * Created by Katharina on 05.06.17.
 */

public class GameDataServerTests {

    GameData data;
    @Before
    public void init() {
        data = new GameData();
    }


    @Test
    public void addPlayerCorrectlyAddsPlayer() {
        Player player = new Player();

        data.addPlayer(player);

        assertEquals(1, data.getPlayers().size());
    }

    @Test
    public void addPlayerAddsMultiplePlayers() {
        data.addPlayer(new Player());
        data.addPlayer(new Player());
        data.addPlayer(new Player());
        data.addPlayer(new Player());

        assertEquals(4, data.getPlayers().size());
    }

    @Test
    public void getPlayersReturnsCorrectPlayers() {
        Player player1 = new Player();
        player1.setPlayerName("Player1");

        data.addPlayer(player1);
        Player obtained = data.getPlayers().get(0);

        assertSame(player1, obtained);
        assertEquals(player1.getPlayerName(), obtained.getPlayerName());
    }

    @Test
    public void getCurrentPlayerReturnsCorrectPlayer() {
        Player playerCurrent = new Player();
        playerCurrent.setPlayerName("PlayerCurrent");

        data.addPlayer(new Player());
        data.addPlayer(new Player());
        data.addPlayer(new Player());
        data.addPlayer(playerCurrent);

        data.setCurrentPlayer(playerCurrent.getPlayerName());

        Player obtained = data.getCurrentPlayer();

        assertSame(playerCurrent, obtained);
    }

    @Test
    public void getDiceRollReturnsCorrectValues() {
        int[] diceValues = {1,2,3};
        data.setDiceRoll(diceValues);

        int[] obtained = data.getDiceRoll();

        for (int i = 0; i < diceValues.length; i++) {
            assertEquals(diceValues[i], obtained[i]);

        }
    }
}
