package gmbh.norisknofun.game.statemachine.server;

import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.attack.evaluatedice.AttackResult;
import gmbh.norisknofun.game.networkmessages.attack.evaluatedice.DiceAmount;
import gmbh.norisknofun.game.networkmessages.attack.evaluatedice.DiceResult;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by pippp on 15.05.2017.
 */

public class EvaluateDiceResultState extends State {

    private ServerContext context;
    private AttackState attackState;
    private final GameDataServer data;
    public EvaluateDiceResultState(ServerContext context, AttackState state){

        this.context=context;
        this.data=this.context.getGameData();
        this.attackState=state;
        sendDiceAmountToPlayers();
    }

    @Override
    public void handleMessage(String senderId, Message message) {
        if(message.getType().equals(DiceResult.class)){
            handleDiceResult(senderId,(DiceResult)message);
        }
    }

    private void handleDiceResult(String senderId, DiceResult message){


        if(getAttackerId().equals(senderId)){
            data.setAttackerDiceResult(message.getDiceResults());
        }else if(getDefenderId().equals(senderId)){
            data.setDefenderDiceResult(message.getDiceResults());
        }

        if(!isEmpty(data.getDefenderDiceResult()) && !isEmpty(data.getAttackerDiceResult())) {
            int [] result=calculateAttackResult();
            handleAttackResult(result[0],result[1]);
        }

    }

    private void handleAttackResult(int winsOfAttacker, int winsOfDefender){

        data.getAttackerRegion().setTroops(data.getAttackerRegion().getTroops()-winsOfDefender);
        data.getDefendersRegion().setTroops(data.getDefendersRegion().getTroops()-winsOfAttacker);

        if(data.getDefendersRegion().getTroops()<=0){ // if attacker  has won
            /*sendAttackResult(true, getAttackerId());
            sendAttackResult(false,getDefenderId());*/
            broadcastResult(getAttackerId(), getDefenderId());
        }else { // if defender has won
/*            sendAttackResult(true,getDefenderId());
            sendAttackResult(false,getAttackerId());*/
            broadcastResult(getDefenderId(), getAttackerId());
        }

        // also notify all other clients about the result
        //TODO: Combine attacker and defender result in this this broadcast


        context.setState(new ChooseTargetState(context));

    }

    private void broadcastResult(String winnerId, String loserId) {
        AttackResult attackResult= new AttackResult();

        attackResult.setAttackerRegion(data.getAttackerRegion().getName());
        attackResult.setAttackerTroops(data.getAttackerRegion().getTroops());
        attackResult.setDefenderRegion(data.getDefendersRegion().getName());
        attackResult.setDefenderTroops(data.getDefendersRegion().getTroops());
        attackResult.setDefenderRegionOwner(data.getDefendersRegion().getOwner());
        attackResult.setWinnerId(winnerId);
        attackResult.setLoserId(loserId);

        context.sendMessage(attackResult);
    }

    private void sendAttackResult(boolean won, String senderId){
        AttackResult attackResult= new AttackResult();

        attackResult.setAttackerRegion(data.getAttackerRegion().getName());
        attackResult.setAttackerTroops(data.getAttackerRegion().getTroops());
        attackResult.setDefenderRegion(data.getDefendersRegion().getName());
        attackResult.setDefenderTroops(data.getDefendersRegion().getTroops());
        attackResult.setDefenderRegionOwner(data.getDefendersRegion().getOwner());
        attackResult.setWon(won);
        context.sendMessage(attackResult,senderId);

    }

    private int [] calculateAttackResult(){

        int winsOfAttacker=0;
        int winsOfDefender=0;
        int [] defenderDiceResult = data.getDefenderDiceResult();
        int [] attackerDiceResult = data.getAttackerDiceResult();


          while (!isEmpty(defenderDiceResult) && !isEmpty(attackerDiceResult)){
              if(getMaxValue(attackerDiceResult)>getMaxValue(defenderDiceResult)){
                  winsOfAttacker++;
              }else {
                  winsOfDefender++;
              }
          }
        return new int[]{winsOfAttacker,winsOfDefender};
    }

    private int getMaxValue(int [] dice){
        int result=0;
        int index=-1;
        for(int i=0; i<dice.length; i++){
            if(dice[i]>result){
                result=dice[i];
                index=i;
            }
        }
        dice[index]=0;
        return result;
    }

    private boolean isEmpty(int [] dice){
        boolean check=true;
        for(int i=0; i<dice.length; i++){
            if(dice[i]!=0){
                check=false;
            }
        }
        return check;
    }
    private void sendDiceAmountToPlayers(){

        DiceAmount diceAmount= new DiceAmount(data.getAttackingTroops());
        context.sendMessage(diceAmount,getAttackerId());

        diceAmount= new DiceAmount(data.getDefendersRegion().getTroops()<2? 1:2);
        context.sendMessage(diceAmount,getDefenderId());

    }

    private String getDefenderId(){
        return data.getPlayerByName(data.getDefendersRegion().getOwner()).getId();
    }

    private String getAttackerId(){
        return data.getPlayerByName(data.getAttackerRegion().getOwner()).getId();
    }
}
