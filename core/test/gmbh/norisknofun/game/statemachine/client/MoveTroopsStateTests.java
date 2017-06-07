package gmbh.norisknofun.game.statemachine.client;

import org.junit.Before;
import org.junit.Test;

import gmbh.norisknofun.assets.AssetFactory;
import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.networkmessages.common.NextPlayer;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Created by Katharina on 30.05.2017.
 */

public class MoveTroopsStateTests {

    GameData data;
    ClientContext context;
    String players[] = {"Franz","Michael","Hubert"};
    @Before
    public void setup() {
        data = new GameData();
        context = new ClientContext(mock(gmbh.norisknofun.game.client.OutboundMessageHandler.class), data);
        context.setState(new MoveTroopsState(context));
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
    public void SetNextPlayerAndSwitchState() {
        BasicMessageImpl message = new NextPlayer(players[1]);
        context.delegateMessage(message);
        assertEquals(players[1], data.getCurrentplayer().getPlayername());
        assertThat(context.getState(), instanceOf(WaitingForNextTurnState.class));
    }

    @Test
    public void NullPlayernameWillNotBeSet() {

        data.setCurrentplayer(players[0]);

        BasicMessageImpl message = new NextPlayer(null);
        context.delegateMessage(message);
        assertEquals(players[0], data.getCurrentplayer().getPlayername());

    }
}
