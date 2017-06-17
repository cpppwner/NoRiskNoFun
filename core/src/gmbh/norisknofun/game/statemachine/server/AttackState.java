package gmbh.norisknofun.game.statemachine.server;

import com.badlogic.gdx.Gdx;

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
