package gmbh.norisknofun.game.statemachine.server;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by pippp on 15.05.2017.
 */

class AttackState extends State {

    private State state;
    AttackState(ServerContext context){
        this.state = new ChooseTroopAmountState(context, this);

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

        this.state.exit();
        this.state = state;
        this.state.enter();

        Gdx.app.log(getClass().getSimpleName(), this.state.getClass().getSimpleName() + " -> " + state.getClass().getSimpleName());
    }
}
