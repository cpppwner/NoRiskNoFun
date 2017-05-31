package gmbh.norisknofun.game.networkmessages.common;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by Katharina on 19.05.2017.
 */

public class SpawnTroopCheck extends BasicMessageImpl {
    public boolean canspawn=false;
    public String errormessage="";

    public SpawnTroopCheck(boolean canspawn, String errormessage) {
        this.canspawn = canspawn;
        this.errormessage = errormessage;
    }
}
