package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.gamemessages.gui.SpawnTroopGui;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.common.SpawnTroop;
import gmbh.norisknofun.game.networkmessages.common.SpawnTroopCheck;
import gmbh.norisknofun.game.networkmessages.distribution.AddTroops;
import gmbh.norisknofun.game.networkmessages.spread.PlayerSpreadFinished;
import gmbh.norisknofun.game.statemachine.State;
//import gmbh.norisknofun.network.Client;

/**
 * Created by Katharina on 19.05.2017.
 */

public class DistributionState extends State {

    private ClientContext context;
    public DistributionState(ClientContext context){

        this.context=context;
    }


    @Override
    public void handleMessage(String senderId, Message message) {

        if(message.getType().equals(AddTroops.class)){
            addTroops(((AddTroops)message).getAmount());
        }else if(message.getType().equals(SpawnTroop.class)){
            doSpawnTroop((SpawnTroop)message);
        }else if(message.getType().equals(SpawnTroopCheck.class)){
            handleSpawnTroopCheckMessage((SpawnTroopCheck)message);

        }else if(message.getType().equals(SpawnTroopGui.class)){
            requestSpawn((SpawnTroopGui)message);
        }else if(message.getType().equals(PlayerSpreadFinished.class)){
            context.setState(new ChooseTargetState(context));
        }
        else {
            Gdx.app.log("DistributionState","unknown message");
        }
    }

    private void addTroops(int amount){
        if(amount>=0)
        context.getGameData().getCurrentPlayer().setTroopToSpread(amount);
    }
    private void requestSpawn(SpawnTroopGui message) {
        context.sendMessage(new SpawnTroop(message.getRegionName()));
    }

    private void doSpawnTroop(SpawnTroop message) {
        context.getGameData().setGuiChanges(new SpawnTroopGui(message.getRegionname(),message.getId()));
    }

    private void handleSpawnTroopCheckMessage(SpawnTroopCheck message){
        if(!message.isCanspawn()){
           context.getGameData().setLastError(message.getErrormessage());
        }
    }
}
