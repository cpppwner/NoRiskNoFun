package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by Katharina on 19.05.2017.
 */

class AttackState extends State {

    private State state;

    AttackState(ClientContext context, boolean isAttacker){
        Gdx.app.log("ATTACK STATE","ENTERED");

        if (isAttacker) {
            state = new ChooseTroopAmountState(context, this);
        } else {
            state = new EvaluateDiceResultState(context);
        }
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
