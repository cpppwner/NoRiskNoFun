package gmbh.norisknofun.game.networkmessages.attack.choosetroops;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 15.05.2017.
 */

public class ChooseTroopsAmountCheck extends BasicMessageImpl {
   public boolean check;

    public ChooseTroopsAmountCheck(boolean check) {
        this.check = check;
    }
}
