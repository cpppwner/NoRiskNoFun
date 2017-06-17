package gmbh.norisknofun.game.networkmessages.spread;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 *
 * After Start Game,GameServer should choose which Player starts an then send this Message
 * Tell the Player he should Spread his troops
 *
 * Server to Clients
 */

public class PlayerSpreadFinished extends BasicMessageImpl implements Serializable {

    private static final long serialVersionUID = 1L;

    private String currentPlayerName;

    public PlayerSpreadFinished() {
    }

    public PlayerSpreadFinished(String currentPlayerName) {
        this.currentPlayerName = currentPlayerName;
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public void setCurrentPlayerName(String currentPlayerName) {
        this.currentPlayerName = currentPlayerName;
    }
}
