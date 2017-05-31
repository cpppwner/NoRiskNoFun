package gmbh.norisknofun.game.networkmessages.attack.evaluatedice;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 15.05.2017.
 */

public class AttackResult extends BasicMessageImpl {
    public String playername;

    public AttackResult(String playername) {
        this.playername = playername;
    }
}
