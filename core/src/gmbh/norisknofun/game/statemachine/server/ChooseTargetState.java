package gmbh.norisknofun.game.statemachine.server;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.assets.AssetMap;
import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.networkmessages.Message;
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
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void handleMessage(String senderId, Message message) {

        if(message.getType().equals(NoAttack.class)){
            context.setState(new MoveTroopsState(context));
        }else if(message.getType().equals(AttackRegion.class)){
            attackRegion(senderId,(AttackRegion)message);
        }else{
            Gdx.app.log("ChooseTargetState","unknown message");
        }
    }

    private void attackRegion(String senderId, AttackRegion message) {

        if(checkIfCurrentPlayer(senderId)){

            if(checkAttackedRegion(senderId,message)){

                data.setDefendersRegion(data.getRegionByName(message.getAttackedRegion()));
                data.setAttackerRegion(data.getRegionByName(message.getOriginRegion()));

                sendAttackRegionCheckMessage(senderId,true);

                context.setState(new AttackState(context));
            }
        }
    }

    private boolean checkIfCurrentPlayer(String senderId){
        if(data.getPlayerById(senderId).equals(data.getCurrentplayer()))
            return true;

        sendAttackRegionCheckMessage(senderId,false);
        return false;
    }

    /**
     * Check if attacked region is hostile region
     * and neighbouring region of originregion
     *
     * @param message
     * @return
     */
    private boolean checkAttackedRegion(String senderId, AttackRegion message){
        boolean check=true;
        AssetMap.Region attackedRegion= data.getRegionByName(message.getAttackedRegion());
        AssetMap.Region orginRegion= data.getRegionByName(message.getOriginRegion());

        if(data.getCurrentplayer().getPlayerName().equals(attackedRegion.getOwner()))
            check=false;

        if(!orginRegion.getNeighbouringRegions().contains(attackedRegion.getName()))
            check=false;


        if(!check)
            sendAttackRegionCheckMessage(senderId,false);

        return check;
    }
    private void sendAttackRegionCheckMessage(String senderId, boolean check){
        AttackRegionCheck  attackRegionCheckmessage= new AttackRegionCheck(check);
        context.sendMessage(attackRegionCheckmessage,senderId);
    }


}
