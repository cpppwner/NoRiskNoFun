package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.assets.AssetMap;
import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.attack.evaluatedice.AttackResult;
import gmbh.norisknofun.game.networkmessages.attack.evaluatedice.IsAttacked;
import gmbh.norisknofun.game.networkmessages.common.NextPlayer;
import gmbh.norisknofun.game.statemachine.State;


/**
 * Created by Katharina on 31.05.2017.
 */

public class WaitingForNextTurnState extends State {

    private ClientContext context;
    private final GameData data;

    public WaitingForNextTurnState(ClientContext context){
        this.context=context;
        this.data=context.getGameData();
    }


    @Override
    public void handleMessage(String senderId, Message message) {
        if(message.getType().equals(NextPlayer.class)){
            setNextPlayer(((NextPlayer)message).getPlayername());
        } else if (message.getType().equals(IsAttacked.class)) {
            context.setState(new AttackState(context, false));
        } else if (message.getType().equals(AttackResult.class)) { // react on broadcast attack result as we have to update the regions
            updateRegions((AttackResult)message);
        }
        else{
            Gdx.app.log("WaitingForNextTurnState","unknown messgae:"+message.getType().getSimpleName());
        }
    }

    private void updateRegions(AttackResult message) {
        AssetMap.Region attackerRegion = context.getGameData().getMapAsset().getRegion(message.getAttackerRegion());
        AssetMap.Region defenderRegion = context.getGameData().getMapAsset().getRegion(message.getDefenderRegion());

        attackerRegion.setTroops(message.getAttackerTroops());

        defenderRegion.setTroops(message.getDefenderTroops());
        defenderRegion.setOwner(message.getDefenderRegionOwner());

        Gdx.app.log("EvaluateDiceResultState", "Attacker Region: " + attackerRegion.getName() +
                ", Troops: " + attackerRegion.getTroops());
        Gdx.app.log("EvaluateDiceResultState", "Defender Region: " + defenderRegion.getName() +
                ", Troops: " + defenderRegion.getTroops() +
                ", Owner: " + defenderRegion.getOwner());

    }

    private void setNextPlayer(String playerName){
        if(playerName!=null){
            data.setCurrentPlayer(playerName);

            if(data.isMyTurn()){
                context.setState(new DistributionState(context));
            }
        }
    }
}
