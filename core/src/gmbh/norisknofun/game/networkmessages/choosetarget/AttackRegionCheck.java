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
    private String errorMessage="";

    public AttackRegionCheck(boolean attackreachable, String errorMessage) {
        this.attackreachable = attackreachable;
        this.errorMessage=errorMessage;
    }

    public boolean isAttackreachable() {
        return attackreachable;
    }

    public void setAttackreachable(boolean attackreachable) {
        this.attackreachable = attackreachable;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
