package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.client.OutboundMessageHandler;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by Katharina on 19.05.2017.
 */

public class ClientContext  {

    private final OutboundMessageHandler outboundMessageHandler;
    private final GameData data;
    private State state;

    public ClientContext(OutboundMessageHandler outboundMessageHandler, GameData data){
        this.outboundMessageHandler = outboundMessageHandler;
        this.data = data;

        resetState();
    }

    public void setState(State state) {

        if (state == null) {
            throw new IllegalArgumentException("state is null");
        }

        Gdx.app.log(getClass().getSimpleName(), this.state.getClass().getSimpleName() + " -> " + state.getClass().getSimpleName());
        data.setCurrentStateName(state.getClass().getSimpleName());
        data.setChangedFlag(true);

        this.state.exit();
        this.state=state;
        this.state.enter();
    }

    public State getState(){
        return state;
    }

    public void delegateMessage(Message message){

        state.handleMessage("", message); // work with interfaces
    }

    public void sendMessage(Message message){

        outboundMessageHandler.handle(message);
    }

    public GameData getGameData(){
        return data;
    }

    public void resetState() {

        state = new ConnectingState(this);
        state.enter();
    }
}
