package gmbh.norisknofun.game.networkmessages.choosetarget;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 15.05.2017.
 */

public class NoAttack extends BasicMessageImpl implements Serializable{
    private static final long serialVersionUID = 1L;

    public NoAttack() {
        super();
    }
}
