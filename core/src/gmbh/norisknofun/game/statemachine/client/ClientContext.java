package gmbh.norisknofun.game.statemachine.client;

import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by Katharina on 19.05.2017.
 */

public class ClientContext  {

    private GameData data;
    private State state;

    public ClientContext(GameData data){
        this.data=data;
    }

    public void setState(State state){
        this.state=state;
    }

    public State getState(){
        return state;
    }

    public void delegateMessage(BasicMessageImpl message){
        state.handleMessage("",message);
    }

    public void sendMessage(BasicMessageImpl message){
        //todo serverdispatcher is still missing
    }

    public GameData getGameData(){
        return data;
    }
}
