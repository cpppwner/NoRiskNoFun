package gmbh.norisknofun.game.networkmessages.waitingforplayers;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by philipp on 13.06.2017.
 */


/**
 * If Player cannot Join
 *
 * Server -> Client
 */



public class PlayerRejected extends BasicMessageImpl implements Serializable {


    private final String reason="Player with same Name not allowed";

    public String getReason() {
        return reason;
    }


}
