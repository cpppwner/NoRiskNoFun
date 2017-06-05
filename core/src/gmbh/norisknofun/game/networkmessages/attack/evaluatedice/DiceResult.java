package gmbh.norisknofun.game.networkmessages.attack.evaluatedice;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 15.05.2017.
 */

public class DiceResult extends BasicMessageImpl implements Serializable{
    private static final long serialVersionUID = 1L;
    private int [] diceResults;

    public DiceResult(int[] diceResults) {
        this.diceResults = diceResults;
    }

    public int[] getDiceResults() {
        return diceResults;
    }

    public void setDiceResults(int[] diceResults) {
        this.diceResults = diceResults;
    }
}
