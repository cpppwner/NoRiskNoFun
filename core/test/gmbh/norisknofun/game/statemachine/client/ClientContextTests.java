package gmbh.norisknofun.game.statemachine.client;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;


import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.client.Client;


public class ClientContextTests {

    GameData data;
    ClientContext context;

    @Before
    public void initialize() {
        data = mock(GameData.class);
        Client client = mock(Client.class);

        context = new ClientContext(client, data);
    }

    @Test
    public void newlyInitializedContextReturnsCorrectStartingState() {
        assertThat(context.getState(), instanceOf(WaitingForPlayersState.class));
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


        assertThat(context.getGameData(), instanceOf(GameData.class));
        assertSame(data, context.getGameData());
    }

}
