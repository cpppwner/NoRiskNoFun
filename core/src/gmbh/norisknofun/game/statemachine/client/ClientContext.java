package gmbh.norisknofun.game.statemachine.client;

import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.client.OutboundMessageHandler;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
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
        state = new ConnectingState(this);
    }

    public void setState(State state) {

        if (state == null) {
            throw new IllegalArgumentException("state is null");
        }

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
}
