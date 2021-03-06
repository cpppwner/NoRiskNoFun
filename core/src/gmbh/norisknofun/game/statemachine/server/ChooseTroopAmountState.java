package gmbh.norisknofun.game.statemachine.server;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.attack.choosetroops.ChooseTroopsAmount;
import gmbh.norisknofun.game.networkmessages.attack.choosetroops.ChooseTroopsAmountCheck;
import gmbh.norisknofun.game.networkmessages.attack.evaluatedice.IsAttacked;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by pippp on 15.05.2017.
 */

class ChooseTroopAmountState extends State {

    private final ServerContext context;
    private AttackState attackState;
    private final GameDataServer  data;
    ChooseTroopAmountState(ServerContext context, AttackState attackState){
        this.context=context;
        this.attackState=attackState;
        this.data=context.getGameData();
    }


    @Override
    public void handleMessage(String senderId, Message message) {
        Gdx.app.log("Server ChooseTroopAmount", "Handling message: " + message.getClass().getName());


        if(message.getType().equals(ChooseTroopsAmount.class)){
            setAttackingTroops(senderId,(ChooseTroopsAmount)message);
        } else {
            Gdx.app.log("ChooseTroopAmountState","unknown message");
        }
    }

    private void setAttackingTroops(String senderId, ChooseTroopsAmount message){

        if(checkTroopAmountMessage(senderId,message)){
            data.setAttackingTroops(message.getAmount());
            sendChooseTroopsAmountCheckMessage(senderId,true,"");
            sendIsAttackedMessage(); // inform defender to change in EvaluateDiceResultState

            attackState.setState(new EvaluateDiceResultState(context));
        }
    }


    private boolean checkTroopAmountMessage(String senderId,ChooseTroopsAmount message){
        boolean check=true;

        if(message.getAmount()>3 || message.getAmount()<1){
            check=false;
            sendChooseTroopsAmountCheckMessage(senderId,false,"Invalid amount of troops");
        }

        if(data.getAttackerRegion().getTroops()-message.getAmount()<1) {
            check = false;
            sendChooseTroopsAmountCheckMessage(senderId,false,"Not enough Troops");
        }


        return check;
    }

    private void sendChooseTroopsAmountCheckMessage(String senderId,boolean check, String errormessage){
        ChooseTroopsAmountCheck chooseTroopsAmountCheck= new ChooseTroopsAmountCheck(check,errormessage);
        context.sendMessage(chooseTroopsAmountCheck,senderId);
    }

    private void sendIsAttackedMessage(){
        IsAttacked isAttacked= new IsAttacked();
        context.sendMessage(isAttacked,data.getPlayerByName(data.getDefendersRegion().getOwner()).getId());

    }
}
