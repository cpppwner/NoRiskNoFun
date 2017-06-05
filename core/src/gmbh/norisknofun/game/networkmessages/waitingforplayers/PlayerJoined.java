package gmbh.norisknofun.game.networkmessages.waitingforplayers;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * After Playernameselected
 *
 * Client -> Server
 */


public class PlayerJoined extends BasicMessageImpl implements Serializable {

    private static final long serialVersionUID = 1L;
    private  String playerName;

    public PlayerJoined(String playerName) {

        this.playerName = playerName;
    }


    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
