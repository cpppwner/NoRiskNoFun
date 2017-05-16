package gmbh.norisknofun.game.networkmessages.waitingforplayers;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * After Playernameselected
 *
 * Client -> Server
 */


public class PlayerJoined extends BasicMessageImpl {

    String playername;


}
