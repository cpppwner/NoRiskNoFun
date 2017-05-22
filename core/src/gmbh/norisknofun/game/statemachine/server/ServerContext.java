package gmbh.norisknofun.game.statemachine.server;

import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

import gmbh.norisknofun.game.statemachine.State;
import gmbh.norisknofun.network.NetworkServer;
import gmbh.norisknofun.network.ServerSessionEventHandlerImpl;
import gmbh.norisknofun.network.Session;
import gmbh.norisknofun.network.SessionImpl;
import gmbh.norisknofun.network.socket.SocketFactory;
import gmbh.norisknofun.network.socket.SocketFactoryImpl;

/**
 * Created by pippp on 17.05.2017.
 */

public class ServerContext {

    private State state;
    private final GameData gameData;
    private NetworkServer networkServer;
    private ServerSessionEventHandlerImpl serverSessionEventHandler;

    public ServerContext(State state, GameData data){
        this.state=state;
        this.gameData=data;
        SocketFactory socketFactory = new SocketFactoryImpl();
        serverSessionEventHandler = new ServerSessionEventHandlerImpl();
        this.networkServer = new NetworkServer(socketFactory, serverSessionEventHandler);
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
