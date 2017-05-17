package gmbh.norisknofun.game.networkmessages.common;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 *
 * After Player moved a Troop
 *
 * Server -> Client
 */


public class MoveTroopCheck extends BasicMessageImpl {

    public String playername;
    //Troop
    //region
    public boolean movePossible;

   


}
