package gmbh.norisknofun.game.networkmessages.attack.evaluatedice;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 15.05.2017.
 */

public class DiceResult extends BasicMessageImpl {
    public int [] diceResults;

    public DiceResult(int[] diceResults) {
        this.diceResults = diceResults;
    }
}
