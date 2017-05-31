package gmbh.norisknofun.game.statemachine.client;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.statemachine.State;
import gmbh.norisknofun.game.statemachine.server.*;
import gmbh.norisknofun.game.statemachine.client.ChooseTargetState;

/**
 * Created by Katharina on 19.05.2017.
 */

public class AttackState extends State {

    private ClientContext context;
    private State state;

    public AttackState(ClientContext context){
        this.context=context;
        state= new ChooseTargetState(context);
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
    public void handleMessage(BasicMessageImpl message) {
        state.handleMessage(message);
    }
}
