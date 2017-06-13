package gmbh.norisknofun.game.statemachine.client;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.mockito.Mockito.*;


import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.client.Client;
import gmbh.norisknofun.game.statemachine.State;


public class ClientContextTests {

    private GameData data;
    private ClientContext context;

    @Before
    public void initialize() {
        data = mock(GameData.class);
        Client client = mock(Client.class);

        context = new ClientContext(client, data);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void newlyInitializedContextReturnsCorrectStartingState() {
        assertThat(context.getState(), instanceOf(ConnectingState.class));
    }


    @Test
    public void ContextSetsStateCorrectly() {
        MoveTroopsState state = mock(MoveTroopsState.class);

        context.setState(state);

        assertThat(context.getState(), instanceOf(MoveTroopsState.class));
    }

    @Test
    public void settingSameStateAgainWorks() {
        MoveTroopsState state = mock(MoveTroopsState.class);

        context.setState(state);
        context.setState(state);

        assertSame(state, context.getState());
    }

    @Test
    public void settingNullStateThrowsException() {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("state is null");

        context.setState(null);

        assertNull(context.getState());
    }


    @Test
    public void contextReturnsGameDataCorrectly() {


        assertThat(context.getGameData(), instanceOf(GameData.class));
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
