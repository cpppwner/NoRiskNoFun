package gmbh.norisknofun.game.statemachine;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * Created by pippp on 15.05.2017.
 */

public abstract class State implements Serializable {

    public void enter() {}
    public void exit() {}
    public abstract void handleMessage(String senderId, Message message);
}
