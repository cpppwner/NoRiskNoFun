package gmbh.norisknofun.game.statemachine.server;

import com.badlogic.gdx.Gdx;

import java.util.List;

import gmbh.norisknofun.assets.impl.map.AssetMap;
import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
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
    public void handleMessage(String senderId,BasicMessageImpl message  ) {
        if(message.getType().equals(MoveTroop.class)){
            moveTroop((MoveTroop)message);
        }else if( message.getType().equals(FinishTurn.class)){
            setNextPlayer();
        }
        else{
            Gdx.app.log("SpreadTroopsState","message unknown");
        }
    }


    private void moveTroop(MoveTroop message){
        List<AssetMap.Region> regions=data.getMapAsset().getRegions();
        int i=0;
        if(message.getPlayername().equals(data.getCurrentplayer().getPlayername())) {
            AssetMap.Region destinationregion = data.getMapAsset().getRegion(message.getDestinationregion());

            if (destinationregion.getOwner().equals(message.getPlayername())) { // check if player is owner of selected region

                broadcastMoveTroopsMessage(message);

            } else {
                sendMoveTroopCheckMessage(message.getPlayername(),false);
            }
        }
    }

    private void setNextPlayer(){

        data.setNextPlayer();
        broadcastNextPlayerMessage();

    }

    private void broadcastNextPlayerMessage(){
        NextPlayer nextPlayer= new NextPlayer(data.getCurrentplayer().getPlayername());
        context.sendMessage(nextPlayer);  //send to all clients
    }

    private void broadcastMoveTroopsMessage(MoveTroop message){
        MoveTroop moveTroop = new MoveTroop(message.getPlayername(),message.getTroopamount(),message.getDestinationregion(),message.getOriginregion());
        context.sendMessage(moveTroop); // send to all clients
    }
    private void sendMoveTroopCheckMessage(String  playername, boolean movepossible){
        MoveTroopCheck response = new MoveTroopCheck(playername,movepossible);
        context.sendMessage(response); // todo how to send to specific client
    }
}
