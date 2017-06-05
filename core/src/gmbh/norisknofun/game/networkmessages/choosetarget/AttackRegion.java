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
 * Client -> Server
 */


public class AttackRegion extends BasicMessageImpl implements Serializable{

    private  String regionname;
    private static final long serialVersionUID = 1L;

    public AttackRegion(String regionname) {
        this.regionname = regionname;
    }

    public String getRegionname() {
        return regionname;
    }

    public void setRegionname(String regionname) {
        this.regionname = regionname;
    }
}
