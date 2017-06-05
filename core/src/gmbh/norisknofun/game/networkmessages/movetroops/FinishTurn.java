package gmbh.norisknofun.game.networkmessages.movetroops;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 15.05.2017.
 */

public class FinishTurn extends BasicMessageImpl implements Serializable{

    private static final long serialVersionUID = 1L;
    public FinishTurn() {
    }
}
