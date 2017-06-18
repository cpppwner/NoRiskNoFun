package gmbh.norisknofun.game.networkmessages.attack.loser;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 15.05.2017.
 */

public class ContinueAttack extends BasicMessageImpl implements Serializable{


    private static final long serialVersionUID = 1L;
    private boolean decision;

    public ContinueAttack(boolean decision) {
        this.decision = decision;
    }

    public boolean isDecision() {
        return decision;
    }

    public void setDecision(boolean decision) {
        this.decision = decision;
    }
}
