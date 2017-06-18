package gmbh.norisknofun.game.networkmessages.waitingforplayers;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by philipp on 13.06.2017.
 */


/**
 * If Player cannot Join
 *
 * Server to Client
 */
public class PlayerRejected extends BasicMessageImpl implements Serializable {


    private final String reason;

    public PlayerRejected(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
