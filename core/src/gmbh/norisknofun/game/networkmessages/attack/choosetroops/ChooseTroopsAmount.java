package gmbh.norisknofun.game.networkmessages.attack.choosetroops;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 15.05.2017.
 */

public class ChooseTroopsAmount extends BasicMessageImpl {
    public int amount;

    public ChooseTroopsAmount(int amount) {
        this.amount = amount;
    }
}