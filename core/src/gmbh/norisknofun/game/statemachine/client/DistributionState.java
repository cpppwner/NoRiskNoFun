package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.common.SpawnTroop;
import gmbh.norisknofun.game.networkmessages.common.SpawnTroopCheck;
import gmbh.norisknofun.game.networkmessages.distribution.AddTroops;
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
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void handleMessage(String senderId, Message message) {

        if(message.getType().equals(AddTroops.class)){
            addTroops(((AddTroops)message).getAmount());
        }else if(message.getType().equals(SpawnTroop.class)){
            // todo interface between statemachine and GUI
        }else if(message.getType().equals(SpawnTroopCheck.class)){
            // todo open dialog with error message
        }
        else {
            Gdx.app.log("DistributionState","unknown message");
        }
    }

    private void addTroops(int amount){
        if(amount>=0)
        context.getGameData().getCurrentplayer().setTroopToSpread(amount);
    }
}
