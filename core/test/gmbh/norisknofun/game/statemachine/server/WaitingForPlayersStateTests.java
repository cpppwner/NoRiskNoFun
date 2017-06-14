package gmbh.norisknofun.game.statemachine.server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Before;
import org.junit.Test;

import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerAccepted;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerJoined;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerRejected;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayersInGame;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.StartGame;
import gmbh.norisknofun.game.server.MessageBus;


public class WaitingForPlayersStateTests {

    private GameDataServer data;
    private ServerContext context;
    private MessageBus messageBusMock;

    @Before
    public void setup() {
        messageBusMock = mock(MessageBus.class);
        data = new GameDataServer();
        context = new ServerContext(data, messageBusMock);
        context.setState(new WaitingForPlayersState(context));
        context.getGameData().setMaxPlayer(10);
    }

    @Test
    public void startGameMessageSwitchesState() {
        BasicMessageImpl message = new StartGame(true);
        Player player = new Player();

        data.getPlayers().addPlayer(player);

        context.handle("SomeID", message);

        assertThat(context.getState(), instanceOf(SpreadTroopsState.class));

        verify(messageBusMock, times(1)).registerInboundMessageHandler(context);
        verify(messageBusMock, times(1)).distributeOutboundMessage(any(StartGame.class));
    }

    @Test
    public void playerJoinedMessageAddsPlayer() {

        Player player = new Player();
        player.setPlayerName("Player1");
        BasicMessageImpl message = new PlayerJoined(player.getPlayerName());

        context.handle("SomeID", message);

        assertEquals(1, data.getPlayers().getPlayerlist().size());
        assertEquals(data.getPlayers().getPlayerlist().get(0).getPlayerName(), player.getPlayerName());

        verify(messageBusMock, times(1)).registerInboundMessageHandler(context);
        verify(messageBusMock, times(1)).distributeOutboundMessage(eq("SomeID"), any(PlayerAccepted.class));
        verify(messageBusMock, times(1)).distributeOutboundMessage(any(PlayersInGame.class));
        verifyNoMoreInteractions(messageBusMock);

    }

    @Test
    public void playerWithSameNameWillNotBeAddedTwice() {
        Player player = new Player();
        player.setPlayerName("Player1");
        BasicMessageImpl message = new PlayerJoined(player.getPlayerName());

        context.handle("SomeID", message);
        context.handle("SomeOtherID", message);

        assertEquals(1, data.getPlayers().getPlayerlist().size());

        verify(messageBusMock, times(1)).registerInboundMessageHandler(context);
        verify(messageBusMock, times(1)).distributeOutboundMessage(eq("SomeID"), any(PlayerAccepted.class));
        verify(messageBusMock, times(1)).distributeOutboundMessage(any(PlayersInGame.class));
        verify(messageBusMock, times(1)).distributeOutboundMessage(eq("SomeOtherID"), any(PlayerRejected.class));
        verifyNoMoreInteractions(messageBusMock);
    }

    @Test
    public void nullPlayerNameWillNotBeAdded() {
        Player player = new Player();
        player.setPlayerName(null);
        BasicMessageImpl message = new PlayerJoined(player.getPlayerName());

        context.handle("SomeID", message);

        assertEquals(0, data.getPlayers().getPlayerlist().size());

        verify(messageBusMock, times(1)).registerInboundMessageHandler(context);
        verify(messageBusMock, times(1)).distributeOutboundMessage(eq("SomeID"), any(PlayerRejected.class));
        verifyNoMoreInteractions(messageBusMock);

    }

    @Test
    public void ifServerIsFullNoNewPlayerIsAdded() {

        context.getGameData().setMaxPlayer(0);
        Message message = new PlayerJoined("someNewPlayer");

        context.handle("someID", message);

        assertThat(context.getGameData().getNumPlayers(), is(0));
        verify(messageBusMock, times(1)).registerInboundMessageHandler(context);
        verify(messageBusMock, times(1)).distributeOutboundMessage(eq("someID"), any(PlayerRejected.class));
        verifyNoMoreInteractions(messageBusMock);
    }
}
