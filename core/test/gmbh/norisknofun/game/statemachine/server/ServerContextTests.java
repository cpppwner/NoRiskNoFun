package gmbh.norisknofun.game.statemachine.server;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.server.MessageBus;
import gmbh.norisknofun.game.server.messaging.MessageBusImpl;
import gmbh.norisknofun.game.statemachine.State;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;


public class ServerContextTests {

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
    public void settingNullStateThrowsException () {

        expectException.expect(IllegalArgumentException.class);
        expectException.expectMessage("state is null");

        context.setState(null);
    }

    @Test
    public void ContextReturnsGameDataCorrectly() {
        GameDataServer data = mock(GameDataServer.class);
        MessageBus messageBus = new MessageBusImpl();

        ServerContext context = new ServerContext(data, messageBus);

        assertThat(context.getGameData(), instanceOf(GameDataServer.class));
        assertSame(data, context.getGameData());
    }

    @Test
    public void enterAndExitMethodsAreInvokedDuringStateTransition() {

        State mockStateOne = mock(State.class);
        State mockStateTwo = mock(State.class);

        context.setState(mockStateOne);

        assertThat(context.getState(), is(sameInstance(mockStateOne)));
        verify(mockStateOne, times(1)).enter();
        verifyNoMoreInteractions(mockStateOne);
        verifyZeroInteractions(mockStateTwo);

        context.setState(mockStateTwo);
        assertThat(context.getState(), is(sameInstance(mockStateTwo)));
        verify(mockStateOne, times(1)).enter();
        verify(mockStateOne, times(1)).exit();
        verifyNoMoreInteractions(mockStateOne);

        verify(mockStateTwo, times(1)).enter();
        verifyNoMoreInteractions(mockStateTwo);
    }

}
