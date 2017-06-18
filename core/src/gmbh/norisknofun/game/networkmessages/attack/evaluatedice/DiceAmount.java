package gmbh.norisknofun.game.networkmessages.attack.evaluatedice;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 15.05.2017.
 */

public class DiceAmount extends BasicMessageImpl implements Serializable {

    private static final long serialVersionUID = 1L;
    private int amount;

    public DiceAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
