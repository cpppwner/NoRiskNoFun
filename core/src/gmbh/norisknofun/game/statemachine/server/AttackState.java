package gmbh.norisknofun.game.statemachine.server;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by pippp on 15.05.2017.
 */

public class AttackState extends State {

    private ServerContext context;
    private State state;
    public AttackState(ServerContext context){
        this.context=context;
    }

    @Override
    public void enter() {
        state.enter();

    }

    @Override
    public void exit() {
        state.exit();
    }

    @Override
    public void handleMessage(String senderId, Message message) {
        state.handleMessage(senderId,message);
    }
}
