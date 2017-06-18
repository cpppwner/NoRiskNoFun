package gmbh.norisknofun.game.gamemessages.gui;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 17.06.2017.
 */

public class ChooseTroopsAmountGui extends BasicMessageImpl{

    private int amount;


    public ChooseTroopsAmountGui(int amount){
        this.amount=amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
