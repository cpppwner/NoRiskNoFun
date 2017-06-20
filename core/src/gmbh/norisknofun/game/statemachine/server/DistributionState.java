package gmbh.norisknofun.game.statemachine.server;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.gamemessages.client.DisconnectClient;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.common.SpawnTroop;
import gmbh.norisknofun.game.networkmessages.common.SpawnTroopCheck;
import gmbh.norisknofun.game.networkmessages.distribution.AddTroops;
import gmbh.norisknofun.game.networkmessages.spread.PlayerSpreadFinished;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by pippp on 17.05.2017.
 */

class DistributionState extends State {

    private final ServerContext context;
    private final GameDataServer data;
    private static final int TROOPS_TO_SPAWN=1;

    DistributionState(ServerContext context){
        this.context=context;
        this.data=this.context.getGameData();
    }

    @Override
    public void enter() {
        addTroopsToPlayer();
        checkIfSomeoneHasWon();
    }

    @Override
    public void handleMessage(String senderId, Message message) {

        try {
            if (message.getType().equals(SpawnTroop.class)) {
                spawnTroop(senderId, (SpawnTroop) message);
            }else{
                Gdx.app.log("Client DistributionState", "unknown message:"+message.getClass().getSimpleName());
            }
        }catch (Exception e){
            Gdx.app.error("DistributionState",e.getMessage(),e);
        }
    }

    private void spawnTroop(String senderId, SpawnTroop message){

        if(checkSpawnMessage(senderId,message)){
            data.getCurrentplayer().setTroopToSpread(data.getCurrentplayer().getTroopToSpread()-1);
            data.getRegionByName(message.getRegionname()).updateTroops(1);
            data.getRegionByName(message.getRegionname()).setOwner(data.getPlayerById(senderId).getPlayerName());
            broadcastSpawnTroopMessage(message);
            Gdx.app.log("Distribution State Spawn Troop", "Troops to spread: " + data.getCurrentplayer().getTroopToSpread());
        }

        if(data.getCurrentplayer().getTroopToSpread()<=0) {
            context.sendMessage(new PlayerSpreadFinished()); // tell client to change state
            context.setState(new ChooseTargetState(context));
        }
    }

    private boolean checkSpawnMessage(String senderId, SpawnTroop spawnTroop){
        boolean check=true;
        if(spawnTroop.getRegionname()==null || spawnTroop.getPlayername()==null){
            check=false;
            sendSpawnTroopCheckMessage(senderId,"Region or playername is null",false);
        }else if(!senderId.equals(data.getCurrentplayer().getId())){
            check=false;
            sendSpawnTroopCheckMessage(senderId,"It's not your turn",false);
        }
        else if(!data.getRegionByName(spawnTroop.getRegionname()).getOwner().equals("none") &&
                !data.getRegionByName(spawnTroop.getRegionname()).getOwner().equals(data.getCurrentplayer().getPlayerName())){
            check=false;
            sendSpawnTroopCheckMessage(senderId,"that is not your region",false);
        }
        return check;
    }

    private void addTroopsToPlayer(){
        data.getCurrentplayer().setTroopToSpread(TROOPS_TO_SPAWN);
        context.sendMessage(new AddTroops(TROOPS_TO_SPAWN), data.getCurrentplayer().getId());
    }

    private void broadcastSpawnTroopMessage(SpawnTroop message){
        Gdx.app.log("Distribution Spawn", "Spawning Troop with ID: " + data.getCurrentFigureId());
        SpawnTroop spawnTroop = new SpawnTroop(message.getRegionname(), data.nextFigureId());
        context.sendMessage(spawnTroop); // send to all clients
    }
    private void sendSpawnTroopCheckMessage(String senderId, String errormessage, boolean movepossible){
        SpawnTroopCheck response = new SpawnTroopCheck(movepossible,errormessage);
        context.sendMessage(response,senderId);
    }

    private void checkIfSomeoneHasWon(){
        int numOfRegion;
        for(Player player: data.getPlayers().getPlayerlist()){
           numOfRegion=data.getNumberOfRegionOwnedByPlayer(player.getPlayerName());
            if(numOfRegion==data.getMapAsset().getRegions().size()){
               sendDisconnectClientMessage();
            }
        }
    }

    private void sendDisconnectClientMessage(){
        context.sendMessage(new DisconnectClient(true));
    }
}
