package gmbh.norisknofun.game.networkmessages.movetroops;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 15.05.2017.
 */

public class FinishTurn extends BasicMessageImpl implements Serializable{

    private static final long serialVersionUID = 1L;
    private String playerName; // also send the name of the next player

    public FinishTurn(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
