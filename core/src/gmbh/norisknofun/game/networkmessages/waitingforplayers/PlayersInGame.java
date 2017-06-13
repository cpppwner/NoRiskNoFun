package gmbh.norisknofun.game.networkmessages.waitingforplayers;

/**
 * Created by Philipp Mödritscher on 13.06.2017.
 */

import java.io.Serializable;
import java.util.List;

import gmbh.norisknofun.game.Players;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 *
 * Server -> Client
 */


public class PlayersInGame extends BasicMessageImpl implements Serializable {

    private Players allPlayers=null; // all joined players

    public static class Player implements Serializable {
        private final String name="";
        private int color;
    }

    public Players getAllPlayers() {
        return allPlayers;
    }

    public void setAllPlayers(Players allPlayers) {
        this.allPlayers = allPlayers;
    }
}
