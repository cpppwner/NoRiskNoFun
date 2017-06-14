package gmbh.norisknofun.game.networkmessages.common;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 14.06.2017.
 */

public class RemoveTroop extends BasicMessageImpl implements Serializable {
    private static final long serialVersionUID = 1L;
    private int amount;
    private String region;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


}
