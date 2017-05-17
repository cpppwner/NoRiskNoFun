package gmbh.norisknofun.game.statemachine.server;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by pippp on 15.05.2017.
 */

public class AttackState implements State {

    private ServerContext context;
    public AttackState(ServerContext context){
        this.context=context;
    }
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
