package gmbh.norisknofun.network.networkmessages.common;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

import gmbh.norisknofun.network.networkmessages.BasicMessageImpl;

/**
 *
 * After Player moved a Troop
 *
 * Server -> Client
 */


public class MoveTroopCheck extends BasicMessageImpl {

    String playername;
    //Troop
    //region
    boolean movePossible;

   


}
