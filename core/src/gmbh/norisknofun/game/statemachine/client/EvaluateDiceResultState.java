package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.assets.AssetMap;
import gmbh.norisknofun.game.gamemessages.gui.DiceResultGui;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.attack.evaluatedice.AttackResult;
import gmbh.norisknofun.game.networkmessages.attack.evaluatedice.DiceAmount;
import gmbh.norisknofun.game.networkmessages.attack.evaluatedice.DiceResult;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by Katharina on 19.05.2017.
 */

public class EvaluateDiceResultState extends State {

    private ClientContext context;
    private AttackState attackState;
    public EvaluateDiceResultState(ClientContext context, AttackState state){
        this.context=context;
        this.attackState=state;
    }
    @Override
    public void handleMessage(String senderId, Message message) {
        if(message.getType().equals(DiceAmount.class)){

            context.getGameData().setAvailableDice(((DiceAmount)message).getAmount());

        }else if(message.getType().equals(DiceResultGui.class)){

            context.sendMessage(new DiceResult(((DiceResultGui)message).getDiceResult()));

        }else if(message.getType().equals(AttackResult.class)){

            handleAttackResult((AttackResult)message);

        }else{
            Gdx.app.log("Client - EvaluateDiceResultState","unknown message:"+message.getClass().getSimpleName());
        }
    }


    private void handleAttackResult(AttackResult attackResult){

        if(context.getGameData().isMyTurn()){ //  I am attacker
            if(attackResult.isWon()){
                     AssetMap.Region attackedRegion= context.getGameData().getMapAsset().getRegion(attackResult.getAttackedRegion());
                     attackedRegion.setOwner(context.getGameData().getMyself().getPlayerName());
                     attackState.setState(new AttackWinnerState());
            }else{
                     attackState.setState(new AttackLoserState());
            }
        }else { //  I am Defender
           if(!attackResult.isWon()){

           }
           context.setState(new WaitingForNextTurnState(context));
        }
    }

}
