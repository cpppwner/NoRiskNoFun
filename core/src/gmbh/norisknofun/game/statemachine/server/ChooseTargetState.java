package gmbh.norisknofun.game.statemachine.server;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.assets.AssetMap;
import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.attack.evaluatedice.IsAttacked;
import gmbh.norisknofun.game.networkmessages.choosetarget.AttackRegion;
import gmbh.norisknofun.game.networkmessages.choosetarget.AttackRegionCheck;
import gmbh.norisknofun.game.networkmessages.choosetarget.NoAttack;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by pippp on 15.05.2017.
 */

public class ChooseTargetState extends State {

    private ServerContext context;
    private final GameDataServer data;
    public ChooseTargetState(ServerContext context){
        this.context=context;
        this.data=context.getGameData();

    }

    @Override
    public void handleMessage(String senderId, Message message) {
        Gdx.app.log("Server ChooseTargetState", "Handling message: " + message.getClass().getName());


        if(message.getType().equals(NoAttack.class)){
            handleNoAttackMessage(senderId,(NoAttack)message);
        }else if(message.getType().equals(AttackRegion.class)){
            handleAttackRegion(senderId,(AttackRegion)message);
        }else{
            Gdx.app.log("ChooseTargetState","unknown message");
        }
    }

    private void handleNoAttackMessage(String senderId, NoAttack message){
        if(data.getCurrentplayer().getId().equals(senderId)){

            context.sendMessage(message, senderId);
            context.setState(new MoveTroopsState(context));
        }else {
            context.sendMessage(new AttackRegionCheck(false,"It's not your turn"), senderId);
        }
    }
    private void handleAttackRegion(String senderId, AttackRegion message) {

        if(message.getDefenderRegion()==null
                || message.getAttackerRegion()==null){
            context.sendMessage(new AttackRegionCheck(false, "Attacker or Defender Region was null."), senderId);
            return;
        }

        Gdx.app.log("ChooseTargetState", "Handle Attack Region from " + senderId);

        if(checkAttackRegionMessage(senderId, message)){
            Gdx.app.log("ChooseTargetState", "Attack check successful");

            data.setDefendersRegion(data.getRegionByName(message.getDefenderRegion()));
            data.setAttackerRegion(data.getRegionByName(message.getAttackerRegion()));
            notifyPlayers(message, senderId);
            context.setState(new AttackState(context));

        }
    }


    private void notifyPlayers(AttackRegion message, String attackerSenderId) {
        sendAttackRegionCheckMessage(attackerSenderId,true,""); // notify the attacker

        // notify the defender
        Player defender = data.getPlayerByName(data.getRegionByName(message.getDefenderRegion()).getOwner());
        context.sendMessage(new IsAttacked(), defender.getId());
    }

    /**
     * Check
     * if message comes from current player
     * if attacked region is hostile region
     * neighbouring region of originregion
     * @param senderId
     * @param message
     * @return true if message is ok
     */
    private boolean checkAttackRegionMessage(String senderId, AttackRegion message){
        boolean check=true;
        AssetMap.Region defenderRegion= data.getRegionByName(message.getDefenderRegion());
        AssetMap.Region attackerRegion= data.getRegionByName(message.getAttackerRegion());

        if (defenderRegion.getOwner().equals("none")) {
            check = false;
            sendAttackRegionCheckMessage(senderId, false, "Can't attack a neutral region.");
        }
        // check if attacker has enough troops on the region
        else if (attackerRegion.getTroops() < 2) {
            check = false;
            sendAttackRegionCheckMessage(senderId, false, "Not enough troops on origin region.");
        } else if(!data.getCurrentplayer().getId().equals(senderId)){
            check=false;
            sendAttackRegionCheckMessage(senderId,false,"It's not your turn");

        } else if(data.getCurrentplayer().getPlayerName().equals(defenderRegion.getOwner())) {
            check = false;
            sendAttackRegionCheckMessage(senderId,false,"You can't attack your own region");
        }
        else if(!attackerRegion.getNeighbouringRegions().contains(defenderRegion.getName())) {
            check = false;
            sendAttackRegionCheckMessage(senderId,false,"Not an neighbouring Region");
        }

        return check;
    }

    private void sendAttackRegionCheckMessage(String senderId, boolean check, String errorMessage){
        AttackRegionCheck  attackRegionCheckmessage= new AttackRegionCheck(check,errorMessage);
        context.sendMessage(attackRegionCheckmessage,senderId);
    }

}
