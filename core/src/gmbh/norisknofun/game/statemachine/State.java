package gmbh.norisknofun.game.statemachine;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * Created by pippp on 15.05.2017.
 */

public abstract class State {

    public void enter() {}
    public void exit() {}
    public abstract void handleMessage(String senderId, Message message);
}
