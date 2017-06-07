package gmbh.norisknofun.game.statemachine.server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import gmbh.norisknofun.assets.impl.AssetFactoryImpl;
import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerJoined;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.StartGame;
import gmbh.norisknofun.game.server.MessageBus;
import gmbh.norisknofun.game.server.messaging.MessageBusImpl;


public class WaitingForPlayersStateTests {

    GameDataServer data;
    ServerContext context;
    @Before
    public void setup() {
        MessageBus messageBus = new MessageBusImpl();
        data = new GameDataServer();
        context = new ServerContext(data, messageBus);
        context.setState(new WaitingForPlayersState(context));
    }

    @Test
    public void StartGameMessageSwitchesState() {
        BasicMessageImpl message = new StartGame(true);
        Player player = new Player();

        data.getPlayers().addPlayer(player);

        context.handle("SomeID", message);

        assertThat(context.getState(), instanceOf(SpreadTroopsState.class));
    }

    @Test
    public void PlayerJoinedMessageAddsPlayer() {

        Player player = new Player();
        player.setPlayername("Player1");
        BasicMessageImpl message = new PlayerJoined(player.getPlayername());



        context.handle("SomeID", message);

        assertEquals(1, data.getPlayers().getPlayers().size());
        assertEquals(data.getPlayers().getPlayers().get(0).getPlayername(), player.getPlayername());

    }

    @Test
    public void PlayerWithSameNameWillNotBeAddedTwice() {
        Player player = new Player();
        player.setPlayername("Player1");
        BasicMessageImpl message = new PlayerJoined(player.getPlayername());

        context.handle("SomeID", message);
        context.handle("SomeID", message);

        assertEquals(1, data.getPlayers().getPlayers().size());
    }

    @Test
    public void NullPlayernameWillNotBeAdded() {
        Player player = new Player();
        player.setPlayername(null);
        BasicMessageImpl message = new PlayerJoined(player.getPlayername());

        context.handle("SomeID", message);

        assertEquals(0, data.getPlayers().getPlayers().size());

    }

}
