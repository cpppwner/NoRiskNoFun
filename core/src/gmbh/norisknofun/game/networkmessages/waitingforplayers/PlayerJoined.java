package gmbh.norisknofun.game.networkmessages.waitingforplayers;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.network.SessionImpl;

/**
 * After Playernameselected
 *
 * Client -> Server
 */


public class PlayerJoined extends BasicMessageImpl {

    public String playername;
    public SessionImpl session=null;

    public PlayerJoined(String playername) {
        this.playername = playername;
    }


}
