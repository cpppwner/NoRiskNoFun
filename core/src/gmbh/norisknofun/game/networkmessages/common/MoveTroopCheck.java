package gmbh.norisknofun.game.networkmessages.common;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 *
 * After Player moved a Troop
 *
 * Server -> Client
 */


public class MoveTroopCheck extends BasicMessageImpl implements Serializable {

    private static final long serialVersionUID = 1L;
    private  String playername;
    //Troop
    //region
    private  boolean movePossible;

    public MoveTroopCheck(String playername, boolean movePossible) {
        this.playername = playername;
        this.movePossible = movePossible;
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public boolean isMovePossible() {
        return movePossible;
    }

    public void setMovePossible(boolean movePossible) {
        this.movePossible = movePossible;
    }
}

