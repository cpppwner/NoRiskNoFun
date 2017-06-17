package gmbh.norisknofun.game.statemachine.server;


import org.junit.Before;
import org.junit.Test;

import java.awt.Color;

import gmbh.norisknofun.GdxTest;
import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.networkmessages.common.SpawnTroop;
import gmbh.norisknofun.game.server.MessageBus;
import gmbh.norisknofun.game.server.messaging.MessageBusImpl;


public class SpreadTroopsStateTests extends GdxTest {
    GameDataServer data;
    ServerContext context;
    Player player;
    Player player2;

    @Before
    public void setup() {
        MessageBus messageBus = new MessageBusImpl(); // can't mock because it's a final class
        data = new GameDataServer();
        player = new Player("Player1", "123", Color.RED.getRGB());
        player2 = new Player("Player2", "987",Color.RED.getRGB());


        data.getPlayers().addPlayer(player);
        data.getPlayers().addPlayer(player2);

        context = new ServerContext(data, messageBus);
        context.setState(new SpreadTroopsState(context));
    }

    @Test
    public void nullNameWillNotThrowException() {
        SpawnTroop message = new SpawnTroop("Otherregion");
        message.setPlayername(null);

        context.handle("123", message);

    }

    @Test
    public void nullRegionWillNotThrowException() {
        SpawnTroop message = new SpawnTroop(null);

        context.handle("123", message);
    }



}
