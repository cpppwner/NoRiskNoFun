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

       setState(new ChooseTroopAmountState(context,this));
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

    public void setState(State state){
        if (state == null) {
            throw new IllegalArgumentException("state is null");
        }

        Gdx.app.log(getClass().getSimpleName(), this.state.getClass().getSimpleName() + " -> " + state.getClass().getSimpleName());

        this.state.exit();
        this.state = state;
        this.state.enter();
    }
}
