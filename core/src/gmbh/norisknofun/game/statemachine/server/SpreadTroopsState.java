package gmbh.norisknofun.game.statemachine.server;

import com.badlogic.gdx.Gdx;

import java.util.List;

import gmbh.norisknofun.assets.AssetMap;
import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.common.MoveTroop;
import gmbh.norisknofun.game.networkmessages.common.SpawnTroop;
import gmbh.norisknofun.game.networkmessages.common.SpawnTroopCheck;
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
    public void handleMessage(String senderId, Message message) {


        if (message.getType().equals(SpawnTroop.class)){
            spawnTroopOnRegion((SpawnTroop)message);
        } else if (message.getType().equals(MoveTroop.class)) {
            context.sendMessage(message);
        }
        else{
            Gdx.app.log("SpreadTroopsState","message unknown");
        }
    }


    private void spawnTroopOnRegion(SpawnTroop message) {

        // todo: Temporarily disabled the check for testing purposes

        /*        //no used field should be null or 0
        if (message.getRegionname() == null
                || message.getPlayername() == null) {
            return;
        }

        List<AssetMap.Region> regions = data.getMapAsset().getRegions();
        int i = 0;

        // check if message comes from current player
        if (message.getPlayername().equals(data.getCurrentplayer().getPlayerName())) {
            AssetMap.Region destinationregion = data.getMapAsset().getRegion(message.getRegionname());

            if (destinationregion.getOwner() == null || destinationregion.getOwner().equals(message.getPlayername())) { // check if player is owner of selected region
                assignRegionsToPlayer();
                broadcastSpawnTroopMessage(message);
                setNextPlayer();

            } else {
                sendSpawnTroopCheckMessage(false);
            }

        }*/
        message.setId(data.nextFigureId());
        context.sendMessage(message);
    }
    private void setNextPlayer(){
        if(currentplayerindex>data.getPlayers().getPlayerlist().size()-1){
            currentplayerindex=0;
        }else {
            currentplayerindex++;
        }
        setCurrentPlayer(currentplayerindex);

    }


    private void setCurrentPlayer(int playerindex){
        data.setCurrentplayer(data.getPlayers().getPlayerlist().get(playerindex).getPlayerName());
        String playername =data.getCurrentplayer().getPlayerName();
        PlayerSpread playerSpread = new PlayerSpread(playername,true);


        context.sendMessage(playerSpread,data.getCurrentplayer().getId());
    }

    private void assignRegionsToPlayer(){

    }

    private void assignTroopsToPlayer(){

    }

    private void broadcastSpawnTroopMessage(SpawnTroop message){
        SpawnTroop spawnTroop = new SpawnTroop(message.getPlayername(),message.getRegionname());
        context.sendMessage(spawnTroop); // send to all clients
    }

    private void sendSpawnTroopCheckMessage(boolean spawnpossible){
        SpawnTroopCheck response = new SpawnTroopCheck(spawnpossible,"can't spawn on this region");
        context.sendMessage(response,data.getCurrentplayer().getId());
    }
}
