package gmbh.norisknofun.game.statemachine.server;

import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.server.InboundMessageHandler;
import gmbh.norisknofun.game.server.MessageBus;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by pippp on 17.05.2017.
 */

public class ServerContext implements InboundMessageHandler {

    private State state;
    private final GameDataServer gameData;
    private final MessageBus messageBus;

    public ServerContext(GameDataServer data, MessageBus messageBus){
        this.gameData=data;
        this.messageBus = messageBus;
        this.state=new WaitingForPlayersState(this);
        this.messageBus.registerInboundMessageHandler(this);
    }

    public void setState(State state){
        this.state=state;
    }

    public State getState(){
        return this.state;
    }

    public void sendMessage(BasicMessageImpl message){

       messageBus.distributeOutboundMessage(message);

        //todo send message via message bus
        // either via messageBus.distributeOutboundMessage(message); --> to send it to all clients
        // or via messageBus.distributeOutboundMessage(id, message); --> to send it to the client with id only

    }

    public void sendMessage(BasicMessageImpl message, String id){
        messageBus.distributeOutboundMessage(id,message);
    }

    public GameDataServer getGameData(){
        return gameData;
    }

    @Override
    public void handle(String senderId, Message message) {
        // TODO delegate this message to the appropriate state
        // note the senderId is a unique identifier identifying the client who sent the message
        // senderId is equal to Client#getId() method
        state.handleMessage(senderId,(BasicMessageImpl)message);

    }
}
