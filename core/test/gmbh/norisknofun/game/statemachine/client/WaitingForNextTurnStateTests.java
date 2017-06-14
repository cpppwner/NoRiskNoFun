package gmbh.norisknofun.game.statemachine.client;

import org.junit.Before;
import org.junit.Test;

import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.networkmessages.common.NextPlayer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by Katharina on 30.05.2017.
 */

public class WaitingForNextTurnStateTests {

    GameData data;
    ClientContext context;
    String players[] = {"Franz","Michael","Hubert"};
    @Before
    public void setup() {
        data = new GameData();
        context = new ClientContext(mock(gmbh.norisknofun.game.client.OutboundMessageHandler.class), data);
        context.setState(new WaitingForNextTurnState(context));
        addPlayers();

    }

    private void addPlayers(){

        Player player;
        for(int i=0; i<players.length; i++){
            player= new Player();
            player.setIshost(false);
            player.setPlayerName(players[i]);
            player.setTroopToSpread(0);
            data.addPlayer(player);
        }

        data.setCurrentPlayer(players[0]);
    }


    @Test
    public void SetNextPlayer() {
        BasicMessageImpl message = new NextPlayer(players[1]);
        context.delegateMessage(message);
        assertEquals(players[1], data.getCurrentPlayer().getPlayerName());

    }

    @Test
    public void NullPlayernameWillNotBeSet() {

        data.setCurrentPlayer(players[0]);

        BasicMessageImpl message = new NextPlayer(null);
        context.delegateMessage(message);
        assertEquals(players[0], data.getCurrentPlayer().getPlayerName());

    }
}
