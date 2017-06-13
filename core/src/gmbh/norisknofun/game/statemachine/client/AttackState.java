package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by Katharina on 19.05.2017.
 */

public class AttackState extends State {

    private ClientContext context;
    private State state;

    public AttackState(ClientContext context){
        this.context=context;
        Gdx.app.log("ATTACK STATE","ENTERED");

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
    public void handleMessage(String senderId, Message message) {
        state.handleMessage(senderId,message);
    }
}
