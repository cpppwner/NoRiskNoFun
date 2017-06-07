package gmbh.norisknofun.game.statemachine.server;

import com.badlogic.gdx.Gdx;

import java.util.List;

import gmbh.norisknofun.assets.AssetMap;
import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.networkmessages.common.MoveTroop;
import gmbh.norisknofun.game.networkmessages.common.MoveTroopCheck;
import gmbh.norisknofun.game.networkmessages.spread.PlayerSpread;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by pippp on 15.05.2017.
 */

public class SpreadTroopsState extends State {

    private ServerContext context;
    private final GameDataServer data;
    private int currentplayerindex=0;
    public SpreadTroopsState(ServerContext context){

        this.context=context;
        data=context.getGameData();
        assignRegionsToPlayer();
        assignTroopsToPlayer();
        setCurrentPlayer(currentplayerindex);

    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void handleMessage(String senderId, BasicMessageImpl message) {

        if(message.getType().equals(MoveTroop.class)){
            moveTroop((MoveTroop)message);
        }
//        else if (message.getType().equals(PlayerSpreadCheck.class)){
//            setNextPlayer();
//        }
        else{
            Gdx.app.log("SpreadTroopsState","message unknown");
        }
    }


    private void moveTroop(MoveTroop message){
        //no used field should be null or 0
        if (message.getDestinationregion() == null
                || message.getPlayername() == null
                || message.getTroopamount() <= 0) {
            return;
        }

        List<AssetMap.Region> regions=data.getMapAsset().getRegions();
        int i=0;

        // check if message comes from current player
        if(message.getPlayername().equals(data.getCurrentplayer().getPlayername())) {
            AssetMap.Region destinationregion = data.getMapAsset().getRegion(message.getDestinationregion());

            if (destinationregion.getOwner() == null || destinationregion.getOwner().equals(message.getPlayername())) { // check if player is owner of selected region
                assignRegionsToPlayer();
                broadcastMoveTroopsMessage(message);
                setNextPlayer();

            } else {
               sendMoveTroopCheckMessage(message.getPlayername(),false);
            }
        }
    }

    private void setNextPlayer(){
        if(currentplayerindex>data.getPlayers().getPlayers().size()-1){
            currentplayerindex=0;
        }else {
            currentplayerindex++;
        }
        setCurrentPlayer(currentplayerindex);

    }


    private void setCurrentPlayer(int playerindex){
        data.setCurrentplayer(data.getPlayers().getPlayers().get(playerindex).getPlayername());
        String playername =data.getCurrentplayer().getPlayername();
        PlayerSpread playerSpread = new PlayerSpread(playername,true);


        context.sendMessage(playerSpread,data.getCurrentplayer().getId());
    }

    private void assignRegionsToPlayer(){

    }

    private void assignTroopsToPlayer(){

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
