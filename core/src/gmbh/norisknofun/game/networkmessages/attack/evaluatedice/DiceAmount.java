package gmbh.norisknofun.game.networkmessages.attack.evaluatedice;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 15.05.2017.
 */

public class DiceAmount extends BasicMessageImpl {
    public int amount;

    public DiceAmount(int amount) {
        this.amount = amount;
    }
}
