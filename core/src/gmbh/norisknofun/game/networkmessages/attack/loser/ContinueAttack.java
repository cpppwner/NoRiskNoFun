package gmbh.norisknofun.game.networkmessages.attack.loser;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 15.05.2017.
 */

public class ContinueAttack extends BasicMessageImpl {
    public boolean decision;

    public ContinueAttack(boolean decision) {
        this.decision = decision;
    }
}