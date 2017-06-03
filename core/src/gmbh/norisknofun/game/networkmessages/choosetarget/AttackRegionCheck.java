package gmbh.norisknofun.game.networkmessages.choosetarget;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 *
 * GameServer checks if AttackedPlayer is reachable
 *
 * Server -> Client
 */


public class AttackRegionCheck extends BasicMessageImpl implements Serializable{

    private static final long serialVersionUID = 1L;

    private boolean attackreachable;

    public AttackRegionCheck(boolean attackreachable) {
        this.attackreachable = attackreachable;
    }

    public boolean isAttackreachable() {
        return attackreachable;
    }

    public void setAttackreachable(boolean attackreachable) {
        this.attackreachable = attackreachable;
    }
}
