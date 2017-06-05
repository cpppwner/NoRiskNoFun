package gmbh.norisknofun.game.networkmessages.attack.choosetroops;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 15.05.2017.
 */

public class ChooseTroopsAmount extends BasicMessageImpl implements Serializable{
    private int amount;
    private static final long serialVersionUID = 1L;

    public ChooseTroopsAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
