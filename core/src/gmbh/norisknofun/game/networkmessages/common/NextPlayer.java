package gmbh.norisknofun.game.networkmessages.common;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 17.05.2017.
 */

public class NextPlayer extends BasicMessageImpl{
    public String playername;

    public NextPlayer(String playername) {
        this.playername = playername;
    }
}
