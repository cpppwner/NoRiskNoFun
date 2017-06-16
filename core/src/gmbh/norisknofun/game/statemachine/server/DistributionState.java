package gmbh.norisknofun.game.statemachine.server;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.common.SpawnTroop;
import gmbh.norisknofun.game.networkmessages.common.SpawnTroopCheck;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by pippp on 17.05.2017.
 */

public class DistributionState extends State {

    private ServerContext context;
    private final GameDataServer data;

    public DistributionState(ServerContext context){
        this.context=context;
        this.data=this.context.getGameData();
        addTroopsToPlayer();
        checkIfSomeoneHasWon();
    }

    @Override
    public void handleMessage(String senderId, Message message) {

        try {
            if (message.getType().equals(SpawnTroop.class)) {
                spawnTroop(senderId, (SpawnTroop) message);
            }
        }catch (Exception e){

            Gdx.app.error("DistributionState",e.getMessage());
        }
    }

    private void spawnTroop(String senderId, SpawnTroop message){

        if(checkSpawnMessage(senderId,message)){
            broadcastSpawnTroopMessage(message);
            data.getCurrentplayer().setTroopToSpread(data.getCurrentplayer().getTroopToSpread()-1);
            if(data.getCurrentplayer().getTroopToSpread()==0){
                context.setState(new ChooseTargetState(context));
            }
        }

    }

    private boolean checkSpawnMessage(String senderId, SpawnTroop spawnTroop){
        boolean check=true;
        if(spawnTroop.getRegionname()==null || spawnTroop.getPlayername()==null){
            check=false;
            sendSpawnTroopCheckMessage(senderId,"region or playername are null",false);
        }else if(!senderId.equals(data.getCurrentplayer().getId())){
            check=false;
            sendSpawnTroopCheckMessage(senderId,"you are not the current player",false);
        }else if(!data.getRegionByName(spawnTroop.getRegionname()).getOwner().equals(data.getCurrentplayer().getPlayerName())){
            check=false;
            sendSpawnTroopCheckMessage(senderId,"that is not your region",false);
        }
        return check;
    }

    private void addTroopsToPlayer(){
        data.getCurrentplayer().setTroopToSpread(5);
    }

    private void broadcastSpawnTroopMessage(SpawnTroop message){
        SpawnTroop spawnTroop = new SpawnTroop(message.getPlayername(),message.getRegionname());
        context.sendMessage(spawnTroop); // send to all clients
    }
    private void sendSpawnTroopCheckMessage(String senderId, String errormessage, boolean movepossible){
        SpawnTroopCheck response = new SpawnTroopCheck(movepossible,errormessage);
        context.sendMessage(response,senderId);
    }

    private void checkIfSomeoneHasWon(){
        //todo check if someone has won the game
    }
}
