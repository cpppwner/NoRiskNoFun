package gmbh.norisknofun.game.statemachine;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.network.Session;

/**
 * Created by pippp on 15.05.2017.
 */

public abstract class State implements Serializable {

    public abstract void enter();
    public abstract void exit();
    public abstract void handleMessage(BasicMessageImpl message);
}
