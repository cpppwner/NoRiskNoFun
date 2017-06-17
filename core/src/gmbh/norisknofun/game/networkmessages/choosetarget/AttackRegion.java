package gmbh.norisknofun.game.networkmessages.choosetarget;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 *
 * If Spread is finished player can Attack
 *
 * Client to Server
 */


public class AttackRegion extends BasicMessageImpl implements Serializable{

    private  String originRegion;
    private  String attackedRegion;
    private static final long serialVersionUID = 1L;

    public AttackRegion(String originRegion, String attackedRegion) {
        this.originRegion = originRegion;
        this.attackedRegion=attackedRegion;
    }

    public String getOriginRegion() {
        return originRegion;
    }

    public void setOriginRegion(String originRegion) {
        this.originRegion = originRegion;
    }

    public String getAttackedRegion() {
        return attackedRegion;
    }

    public void setAttackedRegion(String attackedRegion) {
        this.attackedRegion = attackedRegion;
    }
}
