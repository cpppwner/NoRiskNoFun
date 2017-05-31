package gmbh.norisknofun.game.networkmessages.choosetarget;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 *
 * GameServer checks if AttackedPlayer is reachable
 *
 * Server -> Client
 */


public class AttackRegionCheck extends BasicMessageImpl {


    public boolean attackreachable;

    public AttackRegionCheck(boolean attackreachable) {
        this.attackreachable = attackreachable;
    }
}
