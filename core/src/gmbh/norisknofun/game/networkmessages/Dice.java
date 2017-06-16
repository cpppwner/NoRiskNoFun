package gmbh.norisknofun.game.networkmessages;

import java.io.Serializable;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 *
 * /
/**
 *
 * If attackedPLayer is reachable and player has chosen amount of Troops and rolled the Dice
 *
 * Client to Server
 */
public class Dice extends BasicMessageImpl implements Serializable{

    private static final long serialVersionUID = 1L;
    private String playername;
    private int amountofTroops;
    private int diceResult;

    public Dice(String playername, int amountofTroops, int diceResult) {
        this.playername = playername;
        this.amountofTroops = amountofTroops;
        this.diceResult = diceResult;
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public int getAmountofTroops() {
        return amountofTroops;
    }

    public void setAmountofTroops(int amountofTroops) {
        this.amountofTroops = amountofTroops;
    }

    public int getDiceResult() {
        return diceResult;
    }

    public void setDiceResult(int diceResult) {
        this.diceResult = diceResult;
    }
}
