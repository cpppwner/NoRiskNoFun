package gmbh.norisknofun.game.networkmessages.common;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 *
 * After moved a Troops
 *
 * Client -> Server
 */


public class MoveTroop extends BasicMessageImpl implements Serializable{

    private static final long serialVersionUID = 1L;
    private String  playername;
    private int     troopamount;
    private String  destinationregion;
    private String  originregion;

    public MoveTroop(String playername, int troopamount, String destinationregion, String originregion) {
        this.playername = playername;
        this.troopamount = troopamount;
        this.destinationregion = destinationregion;
        this.originregion = originregion;
    }
    public MoveTroop(){
        //to fill the message object via setter
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public int getTroopamount() {
        return troopamount;
    }

    public void setTroopamount(int troopamount) {
        this.troopamount = troopamount;
    }

    public String getDestinationregion() {
        return destinationregion;
    }

    public void setDestinationregion(String destinationregion) {
        this.destinationregion = destinationregion;
    }

    public String getOriginregion() {
        return originregion;
    }

    public void setOriginregion(String originregion) {
        this.originregion = originregion;
    }
}
