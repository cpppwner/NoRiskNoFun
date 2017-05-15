package gmbh.norisknofun.network.networkmessages.choosetarget;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

import gmbh.norisknofun.network.networkmessages.BasicMessageImpl;

/**
 *
 * GameServer checks if AttackedPlayer is reachable
 *
 * Server -> Client
 */


public class AttackRegionCheck extends BasicMessageImpl {


    boolean attackreachable;


}
