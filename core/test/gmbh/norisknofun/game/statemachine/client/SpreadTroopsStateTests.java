package gmbh.norisknofun.game.statemachine.client;

import org.junit.Before;
import org.junit.Test;

import gmbh.norisknofun.assets.impl.AssetLoaderFactoryImpl;
import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.networkmessages.common.NextPlayer;
import gmbh.norisknofun.network.Session;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * Created by Katharina on 29.05.2017.
 */

public class SpreadTroopsStateTests {

    GameData data;
    ClientContext context;
    private String players[] ={"Franz","Hubert","Michael"};
    @Before
    public void setup() {
        data = new GameData(new AssetLoaderFactoryImpl());
        context = new ClientContext(data);
        context.setState(new SpreadTroopsState(context));
        addPlayers();
    }

    private void addPlayers(){

        Player player;
        for(int i=0; i<players.length; i++){
            player= new Player();
            player.setIshost(false);
            player.setPlayername(players[i]);
            player.setTroopToSpread(0);
            data.addPlayer(player);
        }

        data.setCurrentplayer(players[0]);
    }

    @Test
    public void NextPlayerMessageChangeCurrentPlayer() {
        BasicMessageImpl message = new NextPlayer(players[1]);
        context.delegateMessage(message);
        assertEquals(players[1], data.getCurrentplayer().getPlayername());
    }

    @Test
    public void NullPlayernameWillNotBeSet() {

       data.setCurrentplayer(players[0]);

        BasicMessageImpl message= new NextPlayer(null);
        context.delegateMessage(message);
        assertEquals(players[0], data.getCurrentplayer().getPlayername());

    }
}
