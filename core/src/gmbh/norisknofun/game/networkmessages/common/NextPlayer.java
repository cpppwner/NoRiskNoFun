package gmbh.norisknofun.game.networkmessages.common;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 17.05.2017.
 */

public class NextPlayer extends BasicMessageImpl implements Serializable{

    private static final long serialVersionUID = 1L;
    private String playername;

    public NextPlayer(String playername) {
        this.playername = playername;
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }
}
