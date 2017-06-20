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
    public void handleMessage(String senderId, Message message  ) {
        if(message.getType().equals(MoveTroop.class)){
            moveTroop(senderId,(MoveTroop)message);
        }else if( message.getType().equals(FinishTurn.class)){
            setNextPlayer();
        }
        else{
            Gdx.app.log("MoveTroopState","message unknown"+message.getType().getSimpleName());
        }
    }


    private void moveTroop(String senderId, MoveTroop message){

        if(message.getFromRegion()==null
                || message.getToRegion()==null){
            sendMoveTroopCheckMessage(senderId,false,"From Region or To Region is null");
            return;
        }
         if(checkMoveTroopMessage(senderId,message)) {
            data.getRegionByName(message.getFromRegion()).updateTroops(-1);
            data.getRegionByName(message.getToRegion()).updateTroops(1);
            broadcastMoveTroopsMessage(message);
         }
    }

    private boolean checkMoveTroopMessage(String senderId, MoveTroop message){
        boolean check=true;
        AssetMap.Region fromRegion= data.getRegionByName(message.getFromRegion());
        AssetMap.Region toRegion= data.getRegionByName(message.getToRegion());
        if(!senderId.equals(data.getCurrentplayer().getId())){
            check=false;
            sendMoveTroopCheckMessage(senderId,false, "It's not your turn");
        }else if(!toRegion.getOwner().equals(data.getCurrentplayer().getPlayerName())){
            check=false;
            sendMoveTroopCheckMessage(senderId,false,"You can't move troops on enemy region");
        }else if(fromRegion.getTroops()<2){
            check=false;
            sendMoveTroopCheckMessage(senderId,false, "Not enough troops on origin region ");
        }
        return check;
    }

    private void setNextPlayer(){

        data.setNextPlayer();
        broadcastNextPlayerMessage();
        context.setState(new DistributionState(context));

    }

    private void broadcastNextPlayerMessage(){
        NextPlayer nextPlayer= new NextPlayer(data.getCurrentplayer().getPlayerName());
        context.sendMessage(nextPlayer);  //send to all clients
    }

    private void broadcastMoveTroopsMessage(MoveTroop message){
        MoveTroop moveTroop = new MoveTroop(message.getFromRegion(),message.getToRegion(), message.getFigureId());

        context.sendMessage(moveTroop); // send to all clients
    }
    private void sendMoveTroopCheckMessage(String senderId, boolean movePossible, String errorMessage){
        MoveTroopCheck response = new MoveTroopCheck(movePossible,errorMessage );
        context.sendMessage(response,senderId);
    }
}
