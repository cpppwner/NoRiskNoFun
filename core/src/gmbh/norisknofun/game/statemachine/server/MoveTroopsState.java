package gmbh.norisknofun.game.statemachine.server;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.assets.AssetMap;
import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.common.MoveTroop;
import gmbh.norisknofun.game.networkmessages.common.MoveTroopCheck;
import gmbh.norisknofun.game.networkmessages.common.NextPlayer;
import gmbh.norisknofun.game.networkmessages.movetroops.FinishTurn;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by pippp on 15.05.2017.
 */

public class MoveTroopsState extends State {

    private ServerContext context;
    private final GameDataServer data;
    public MoveTroopsState(ServerContext context){


        this.context=context;
        data=context.getGameData();

    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void handleMessage(String senderId, Message message  ) {
        if(message.getType().equals(MoveTroop.class)){
            moveTroop(senderId,(MoveTroop)message);
        }else if( message.getType().equals(FinishTurn.class)){
            setNextPlayer();
        }
        else{
            Gdx.app.log("SpreadTroopsState","message unknown");
        }
    }


    private void moveTroop(String senderId, MoveTroop message){

        if(message.getPlayername().equals(data.getCurrentplayer().getPlayerName())) {
            AssetMap.Region destinationregion = data.getMapAsset().getRegion(message.getToRegion());

            if (destinationregion.getOwner().equals(message.getPlayername())) { // check if player is owner of selected region

                broadcastMoveTroopsMessage(message);

            } else {
                sendMoveTroopCheckMessage(senderId, message.getPlayername(),false);
            }
        }else{
            sendMoveTroopCheckMessage(senderId,message.getPlayername(),false);
        }
    }

    private void setNextPlayer(){

        data.setNextPlayer();
        broadcastNextPlayerMessage();
        context.setState(new ChooseTargetState(context));

    }

    private void broadcastNextPlayerMessage(){
        NextPlayer nextPlayer= new NextPlayer(data.getCurrentplayer().getPlayerName());
        context.sendMessage(nextPlayer);  //send to all clients
    }

    private void broadcastMoveTroopsMessage(MoveTroop message){
        MoveTroop moveTroop = new MoveTroop(message.getPlayername(),message.getTroopamount(),message.getToRegion(),message.getFromRegion(), message.getFigureId());
        context.sendMessage(moveTroop); // send to all clients
    }
    private void sendMoveTroopCheckMessage(String senderId,String  playername, boolean movepossible){
        MoveTroopCheck response = new MoveTroopCheck(playername,movepossible);
        context.sendMessage(response,senderId); // todo how to send to specific client
    }
}
