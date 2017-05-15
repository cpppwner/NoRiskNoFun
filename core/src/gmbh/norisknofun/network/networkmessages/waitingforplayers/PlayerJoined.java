package gmbh.norisknofun.network.networkmessages.waitingforplayers;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

import gmbh.norisknofun.network.networkmessages.BasicMessageImpl;

/**
 * After Playernameselected
 *
 * Client -> Server
 */


public class PlayerJoined extends BasicMessageImpl {

    String playername;


}
