package gmbh.norisknofun.game.statemachine.server;

import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by pippp on 17.05.2017.
 */

public class ServerContext {

    private State state;
    private final GameData gameData;
    public ServerContext(GameData data){

        this.gameData=data;
        this.state=new WaitingForPlayersState(this);
    }

    public void setState(State state){
        this.state=state;
    }

    public State getState(){
        return this.state;
    }
    public void delegateMessage(BasicMessageImpl message){
        state.handleMessage(message);
    }

    public void sendMessage(BasicMessageImpl message){
        //todo serverdispatcher is still missing


    }

    public GameData getGameData(){
        return gameData;
    }

}
