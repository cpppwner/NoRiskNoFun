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

public class DistributionState extends State {

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
        if(message.getPlayername().equals(data.getCurrentplayer().getPlayername())) {

            AssetMap.Region destinationregion = data.getMapAsset().getRegion(message.getDestinationregion());
            if (destinationregion.getOwner().equals(message.getPlayername())) { // check if player is owner of selected region

                broadcastMoveTroopsMessage(message);
                data.getCurrentplayer().setTroopToSpread(data.getCurrentplayer().getTroopToSpread()-1);
                if(data.getCurrentplayer().getTroopToSpread()==0){
                    context.setState(new ChooseTargetState(context));
                }

            } else {
                sendMoveTroopCheckMessage(message.getPlayername(),false);
            }
        }
    }

    private void addTroopsToPlayer(){
        data.getCurrentplayer().setTroopToSpread(5);
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
