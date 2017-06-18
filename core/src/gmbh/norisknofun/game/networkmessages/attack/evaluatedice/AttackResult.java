package gmbh.norisknofun.game.networkmessages.attack.evaluatedice;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 15.05.2017.
 */

public class AttackResult extends BasicMessageImpl implements Serializable{
    private boolean won;
    private String attackedRegion;
    private static final long serialVersionUID = 1L;


    public AttackResult(boolean won, String attackedRegion) {

        this.won = won;
        this.attackedRegion=attackedRegion;
    }


    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public String getAttackedRegion() {
        return attackedRegion;
    }

    public void setAttackedRegion(String attackedRegion) {
        this.attackedRegion = attackedRegion;
    }
}
