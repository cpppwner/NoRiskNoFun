package gmbh.norisknofun.game.statemachine.server;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.assets.AssetMap;
import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.common.NextPlayer;
import gmbh.norisknofun.game.networkmessages.common.SpawnTroop;
import gmbh.norisknofun.game.networkmessages.common.SpawnTroopCheck;
import gmbh.norisknofun.game.networkmessages.spread.PlayerSpreadFinished;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by pippp on 15.05.2017.
 */

public class SpreadTroopsState extends State {

    private ServerContext context;
    private final GameDataServer data;
    private static final int TROOPS_TO_SPREAD = 3;
    public SpreadTroopsState(ServerContext context){

        this.context=context;
        data=context.getGameData();
        assignTroopsToPlayer();
        setFirstPlayerAsCurrent();

    }


    @Override
    public void handleMessage(String senderId, Message message) {

        if (message.getType().equals(SpawnTroop.class)){
            spawnTroopOnRegion(senderId,(SpawnTroop)message);

        }
        else{
            Gdx.app.log("SpreadTroopsState","message unknown: " + message.getClass().getSimpleName());
        }
    }


    private void spawnTroopOnRegion(String senderId, SpawnTroop message) {

        if(checkSpawnMessage(senderId,message)){

            updateRegion(message);
            data.getCurrentplayer().setTroopToSpread(data.getCurrentplayer().getTroopToSpread()-1);
            broadcastSpawnTroopMessage(message);
            setNextPlayer();
            checkTroops(); // when all troops are spread, change state to DistributionState
        }
//        message.setId(data.nextFigureId());
//        System.out.println("server state figure id:"+message.getId());
//        context.sendMessage(message);
    }

    private boolean checkSpawnMessage(String senderId, SpawnTroop message){
        boolean check=true;
        //no used field should be null or 0
        if (message.getRegionname() == null || message.getPlayername()==null) {
            check = false;
            sendSpawnTroopCheckMessage(senderId, false, "Name or Region is null");
        } else if(!data.getCurrentplayer().getId().equals(senderId)){  //if message is not from current player
            check=false;
            sendSpawnTroopCheckMessage(senderId,false,"It's not your turn");
        }else if(data.getCurrentplayer().getTroopToSpread()<=0){ //when player has no troops to spawn
            check=false;
            sendSpawnTroopCheckMessage(senderId,false, "You already spawned all your troops");
        }else if(!data.getRegionByName(message.getRegionname()).getOwner().equals("none")
                && !data.getRegionByName(message.getRegionname()).getOwner().equals(data.getCurrentplayer().getPlayerName())){ // if region is enemy region
            check=false;
            sendSpawnTroopCheckMessage(senderId,false, "You can't spawn on this region");
        }
        return check;
    }

    /**
     * If no player has troops to spread change state to DistributionState
     */
    private void checkTroops(){
        boolean check=false;
        for(Player player: data.getPlayers().getPlayerlist()) {
            if (player.getTroopToSpread() > 0) {
                check = true;
                break;
            }
        }
        if(!check){
            data.setCurrentplayer(data.getPlayers().getPlayerlist().get(0).getPlayerName());
            context.setState(new DistributionState(context));
            context.sendMessage(new PlayerSpreadFinished(context.getGameData().getCurrentplayer().getPlayerName()));
        }
    }
    private void setNextPlayer(){
        data.setCurrentplayer(data.getPlayers().getNextPlayername(data.getCurrentplayer().getPlayerName()));
        NextPlayer nextPlayer = new NextPlayer(data.getCurrentplayer().getPlayerName());
        System.out.println("SERVER: Next Player: " + nextPlayer.getPlayername());
        context.sendMessage(nextPlayer);

    }

    /**
     * set the first player as currentplayer
     */
    private void setFirstPlayerAsCurrent(){
        data.setCurrentplayer(data.getPlayers().getPlayerlist().get(0).getPlayerName());
        NextPlayer nextPlayer = new NextPlayer(data.getCurrentplayer().getPlayerName());
        context.sendMessage(nextPlayer);
    }

    private void updateRegion(SpawnTroop message){
        AssetMap.Region region = data.getRegionByName(message.getRegionname());
        if(region.getOwner().equals("none")) {
            region.setOwner(data.getCurrentplayer().getPlayerName());
        }

        // update the troops on this region
        region.updateTroops(1);
    }

    private void assignTroopsToPlayer(){
        for(Player player: data.getPlayers().getPlayerlist()){
            player.setTroopToSpread(TROOPS_TO_SPREAD);
        }
    }

    private void broadcastSpawnTroopMessage(SpawnTroop message){
        SpawnTroop spawnTroop = new SpawnTroop(message.getRegionname());
        context.sendMessage(spawnTroop); // send to all clients
    }

    private void sendSpawnTroopCheckMessage(String senderId, boolean spawnpossible, String errormessage){
        SpawnTroopCheck response = new SpawnTroopCheck(spawnpossible,errormessage);
        context.sendMessage(response,senderId);
    }
}
