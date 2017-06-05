package gmbh.norisknofun.game.networkmessages.common;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 15.05.2017.
 */

public class MoveTroopsDone extends BasicMessageImpl implements Serializable{

    private String playername;
    private static final long serialVersionUID = 1L;

    public MoveTroopsDone(String playername) {
        this.playername = playername;
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }
}
