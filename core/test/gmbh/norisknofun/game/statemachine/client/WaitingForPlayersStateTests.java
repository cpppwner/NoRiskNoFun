
package gmbh.norisknofun.game.statemachine.client;

import org.junit.Before;
import org.junit.Test;

import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.StartGame;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by Katharina on 29.05.2017.
 */

public class WaitingForPlayersStateTests {

    GameData data;
    ClientContext context;
    @Before
    public void setup() {
        data = new GameData();
        context = new ClientContext(mock(gmbh.norisknofun.game.client.OutboundMessageHandler.class), data);
        context.setState(new WaitingForPlayersState(context));
    }



    @Test
    public void StartGameMessageSwitchesState() {
        BasicMessageImpl message = new StartGame(true);

        context.delegateMessage(message);

        assertThat(context.getState(), instanceOf(SpreadTroopsState.class));
    }



}
