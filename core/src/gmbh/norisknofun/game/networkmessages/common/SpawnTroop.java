package gmbh.norisknofun.game.networkmessages.common;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by Katharina on 19.05.2017.
 */

public class SpawnTroop extends BasicMessageImpl {
    public String playername;
    public String regionname;

    public SpawnTroop(String playername, String regionname) {
        this.playername = playername;
        this.regionname = regionname;
    }
}
