package gmbh.norisknofun.game.statemachine.server;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.server.MessageBus;
import gmbh.norisknofun.game.server.messaging.MessageBusImpl;


public class ServerContextTests {

    GameDataServer data;
    ServerContext context;

    @Before
    public void initialize() {
        data = mock(GameDataServer.class);
        MessageBus messageBus = new MessageBusImpl();

        context = new ServerContext(data, messageBus);
    }

    @Test
    public void newlyInitializedContextReturnsCorrectStartingState() {
        assertThat(context.getState(), instanceOf(WaitingForPlayersState.class));
    }


    @Test
    public void ContextSetsStateCorrectly() {
        SpreadTroopsState state = mock(SpreadTroopsState.class);

        context.setState(state);

        assertThat(context.getState(), instanceOf(SpreadTroopsState.class));
    }

    @Test
    public void settingSameStateAgainWorks() {
        SpreadTroopsState state = mock(SpreadTroopsState.class);

        context.setState(state);
        context.setState(state);

        assertSame(state, context.getState());
    }

    @Test
    public void settingNullStateWorks() {
        MoveTroopsState state = null;

        context.setState(state);

        assertNull(context.getState());
    }

    @Test
    public void switchingFromNullStateWorks() {
        MoveTroopsState state = mock(MoveTroopsState.class);

        context.setState(null);
        context.setState(state);

        assertSame(state, context.getState());
    }

    @Test
    public void ContextReturnsGameDataCorrectly() {
        GameDataServer data = mock(GameDataServer.class);
        MessageBus messageBus = new MessageBusImpl();

        ServerContext context = new ServerContext(data, messageBus);

        assertThat(context.getGameData(), instanceOf(GameDataServer.class));
        assertSame(data, context.getGameData());
    }

}
