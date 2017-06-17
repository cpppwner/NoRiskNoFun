package gmbh.norisknofun.game.gamemessages.gui;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 17.06.2017.
 */

public class DiceResultGui extends BasicMessageImpl{

    private int [] diceResult;

    public DiceResultGui(int [] diceResult){
        this.diceResult=diceResult;
    }

    public int[] getDiceResult() {
        return diceResult;
    }

    public void setDiceResult(int[] diceResult) {
        this.diceResult = diceResult;
    }
}
