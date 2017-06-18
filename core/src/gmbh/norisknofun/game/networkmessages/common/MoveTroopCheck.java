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
 * Server to Client
 */


public class MoveTroopCheck extends BasicMessageImpl implements Serializable {

    private static final long serialVersionUID = 1L;
    private String errorMessage;
    private  boolean movePossible;

    public MoveTroopCheck(boolean movePossible, String errorMessage) {
       this.errorMessage=errorMessage;
        this.movePossible = movePossible;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isMovePossible() {
        return movePossible;
    }

    public void setMovePossible(boolean movePossible) {
        this.movePossible = movePossible;
    }
}

