package gmbh.norisknofun.game.statemachine.server;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.server.MessageBus;
import gmbh.norisknofun.game.server.messaging.MessageBusImpl;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by pippp on 14.06.2017.
 */

public class ChooseTargetStateTests {

    private ServerContext context;

    @Before
    public void initialize() {
        GameDataServer data = mock(GameDataServer.class);
        MessageBus messageBus = new MessageBusImpl();

        context = new ServerContext(data, messageBus);
    }

    @Rule
    public ExpectedException expectException = ExpectedException.none();

    @Test
    public void newlyInitializedContextReturnsCorrectStartingState() {
        assertThat(context.getState(), instanceOf(WaitingForPlayersState.class));
    }
}
