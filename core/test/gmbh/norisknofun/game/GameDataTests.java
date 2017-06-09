package gmbh.norisknofun.game;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import gmbh.norisknofun.assets.AssetMap;


/**
 * Created by Katharina on 05.06.17.
 */

public class GameDataTests {

    GameData data;
    @Before
    public void init() {
        data = new GameData();
    }

    @Ignore
    @Test
    public void getMapAssetReturnsAsset() {


        assertThat(data.getMapAsset(), instanceOf(AssetMap.class));
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
        player1.setPlayername("Player1");

        data.addPlayer(player1);
        Player obtained = data.getPlayers().get(0);

        assertSame(player1, obtained);
        assertEquals(player1.getPlayername(), obtained.getPlayername());
    }

    @Test
    public void getCurrentPlayerReturnsCorrectPlayer() {
        Player playerCurrent = new Player();
        playerCurrent.setPlayername("PlayerCurrent");
        data.setCurrentplayer(playerCurrent.getPlayername());

        data.addPlayer(new Player());
        data.addPlayer(new Player());
        data.addPlayer(new Player());
        data.addPlayer(playerCurrent);

        Player obtained = data.getCurrentplayer();

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
