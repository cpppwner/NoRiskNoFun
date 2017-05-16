package gmbh.norisknofun.game.statemachine;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 15.05.2017.
 */

public interface State {

    public void enter();
    public void exit();
    public void handleMessage(BasicMessageImpl message);
}
