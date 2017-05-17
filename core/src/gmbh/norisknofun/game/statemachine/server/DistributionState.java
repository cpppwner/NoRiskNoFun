package gmbh.norisknofun.game.statemachine.server;


import java.util.List;

import gmbh.norisknofun.assets.impl.map.AssetMap;
import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.networkmessages.common.MoveTroop;
import gmbh.norisknofun.game.networkmessages.common.MoveTroopCheck;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by pippp on 17.05.2017.
 */

public class DistributionState implements State {

    private ServerContext context;
    private final GameData data;

    public DistributionState(ServerContext context){
        this.context=context;
        this.data=this.context.getGameData();
        addTroopsToPlayer();
    }
    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void handleMessage(BasicMessageImpl message) {

        if(message.getType().equals(MoveTroop.class)){
            moveTroop((MoveTroop)message);
        }
    }

    private void moveTroop(MoveTroop message){ //todo Refactor later
        List<AssetMap.Region> regions=context.getGameData().getMapAsset().getRegions();
        int i=0;
        if(message.playername.equals(context.getGameData().getCurrentplayer())) {
            while (!regions.get(i).getName().equals(message.destinationregion) && i < regions.size()) {
                i++;
            }
            if (regions.get(i).getOwner().equals(message.playername)) { // check if player is owner of selected region
                MoveTroop moveTroop = new MoveTroop();
                moveTroop.destinationregion = message.destinationregion;
                moveTroop.originregion = message.originregion;
                moveTroop.playername = message.playername;
                moveTroop.troopamount = message.troopamount;
                context.sendMessage(moveTroop);

                data.getCurrentplayer().setTroopToSpread(data.getCurrentplayer().getTroopToSpread()-1);
                if(data.getCurrentplayer().getTroopToSpread()==0){
                    context.setState(new ChooseTargetState(context));
                }

            } else {
                MoveTroopCheck response = new MoveTroopCheck();
                response.playername = message.playername;
                response.movePossible = false;
                context.sendMessage(response); // todo how to send to specific client
            }
        }
    }

    private void addTroopsToPlayer(){
        data.getCurrentplayer().setTroopToSpread(5);
    }
}
