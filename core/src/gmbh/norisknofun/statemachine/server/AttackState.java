package gmbh.norisknofun.statemachine.server;

import gmbh.norisknofun.network.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.statemachine.State;

/**
 * Created by pippp on 15.05.2017.
 */

public class AttackState implements State {

   private State state;
    @Override
    public void enter() {
        state.enter();

    }

    @Override
    public void exit() {
        state.exit();
    }

    @Override
    public void handleMessage(BasicMessageImpl message) {
        state.handleMessage(message);
    }
}
