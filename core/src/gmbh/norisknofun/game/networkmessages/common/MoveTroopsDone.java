package gmbh.norisknofun.game.networkmessages.common;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 15.05.2017.
 */

public class MoveTroopsDone extends BasicMessageImpl {

    public String playername;

    public MoveTroopsDone(String playername) {
        this.playername = playername;
    }
}
