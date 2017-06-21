package gmbh.norisknofun.game.statemachine;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * A State used to enter other states
 */

public abstract class DelegatingState extends State {

    private State state;

    protected void initializeState(State state) {
        if (this.state != null) {
            throw new IllegalStateException("State already initialized.");
        }
        this.state = state;
        enter();
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
