package gmbh.norisknofun.game.networkmessages.spread;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */


import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * If Spread of Player was correct tell him
 *
 * If check is false Player has to spread again
 * Server to Client
 */

public class PlayerSpreadCheck extends BasicMessageImpl implements Serializable{

    private static final long serialVersionUID = 1L;
    private  String playername;
    private  boolean check;

    public PlayerSpreadCheck(String playername, boolean check) {
        this.playername = playername;
        this.check = check;
    }
    //region


    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
