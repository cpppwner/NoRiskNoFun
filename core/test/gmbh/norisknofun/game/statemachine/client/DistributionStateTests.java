package gmbh.norisknofun.game.statemachine.client;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import gmbh.norisknofun.assets.impl.AssetLoaderFactoryImpl;
import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.networkmessages.distribution.AddTroops;
import gmbh.norisknofun.network.SessionImpl;

import static org.junit.Assert.assertEquals;

/**
 * Created by Katharina on 29.05.2017.
 */

public class DistributionStateTests {

    GameData data;
    ClientContext context;
    @Before
    public void setup() {
        data = new GameData(new AssetLoaderFactoryImpl());
        context = new ClientContext(data);
        context.setState(new DistributionState(context));
        Player player = new Player (mock(SessionImpl.class));
        player.setIshost(false);
        player.setPlayername("Franz");
        player.setTroopToSpread(0);
        data.addPlayer(player);
        data.setCurrentplayer(player.getPlayername());
    }



    @Test
    public void AddTroopsMessageSetTroopsToSpread() {
        BasicMessageImpl message = new AddTroops(5);
        context.delegateMessage(message);
        assertEquals(5, data.getCurrentplayer().getTroopToSpread());
    }

    @Test
    public void NegativeAddTroopsAmountWillNotBeSet() {

        BasicMessageImpl message = new AddTroops(-5);
        context.delegateMessage(message);
        assertEquals(0, data.getCurrentplayer().getTroopToSpread());

    }
}
