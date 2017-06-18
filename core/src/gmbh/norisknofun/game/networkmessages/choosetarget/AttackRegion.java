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

    private  String attackerRegion;
    private  String defenderRegion;
    private static final long serialVersionUID = 1L;

    public AttackRegion(String attackerRegion, String defenderRegion) {
        this.attackerRegion = attackerRegion;
        this.defenderRegion =defenderRegion;
    }

    public String getAttackerRegion() {
        return attackerRegion;
    }

    public void setAttackerRegion(String attackerRegion) {
        this.attackerRegion = attackerRegion;
    }

    public String getDefenderRegion() {
        return defenderRegion;
    }

    public void setDefenderRegion(String defenderRegion) {
        this.defenderRegion = defenderRegion;
    }
}
