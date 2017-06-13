package gmbh.norisknofun.game.statemachine.client;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.networkmessages.distribution.AddTroops;

import static org.junit.Assert.assertEquals;

/**
 * Created by Katharina on 29.05.2017.
 */

public class DistributionStateTests {

    GameData data;
    ClientContext context;
    @Before
    public void setup() {
        data = new GameData();
        context = new ClientContext(mock(gmbh.norisknofun.game.client.OutboundMessageHandler.class), data);
        context.setState(new DistributionState(context));
        Player player = new Player ();
        player.setIshost(false);
        player.setPlayerName("Franz");
        player.setTroopToSpread(0);
        data.addPlayer(player);
        data.setCurrentPlayer(player.getPlayerName());
    }



    @Test
    public void AddTroopsMessageSetTroopsToSpread() {
        BasicMessageImpl message = new AddTroops(5);
        context.delegateMessage(message);
        assertEquals(5, data.getCurrentPlayer().getTroopToSpread());
    }

    @Test
    public void NegativeAddTroopsAmountWillNotBeSet() {

        BasicMessageImpl message = new AddTroops(-5);
        context.delegateMessage(message);
        assertEquals(0, data.getCurrentPlayer().getTroopToSpread());

    }
}
