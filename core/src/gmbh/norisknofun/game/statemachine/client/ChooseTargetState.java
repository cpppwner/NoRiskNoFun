package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.gamemessages.gui.AttackRegionGui;
import gmbh.norisknofun.game.gamemessages.gui.NoAttackGui;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.choosetarget.AttackRegion;
import gmbh.norisknofun.game.networkmessages.choosetarget.AttackRegionCheck;
import gmbh.norisknofun.game.networkmessages.choosetarget.NoAttack;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by Katharina on 19.05.2017.
 */

public class ChooseTargetState extends State {

    private ClientContext context;


    public ChooseTargetState(ClientContext context){
        this.context=context;
    }


    @Override
    public void handleMessage(String senderId, Message message) {
        
       if(message.getType().equals(AttackRegionCheck.class)){
           handleAttackRegionCheckMessage((AttackRegionCheck)message);
        }else if(message.getType().equals(AttackRegionGui.class)){
           requestAttack((AttackRegionGui)message);
        }else if(message.getType().equals(NoAttackGui.class)){ //player doesn't want to attack
           context.sendMessage(new NoAttack());
       }else {
           Gdx.app.log("Client ChooseTargetState", "unknown message:"+message.getClass().getSimpleName());
        }
    }

    private void handleAttackRegionCheckMessage(AttackRegionCheck message){
        if(message.isAttackreachable()){
            context.setState(new AttackState(context));
        }else{
            context.getGameData().setLastError(message.getErrorMessage());
        }
    }

    private void requestAttack(AttackRegionGui message){
        AttackRegion attackRegion= new AttackRegion(message.getOriginRegion(),message.getAttackedRegion());
        context.sendMessage(attackRegion);
    }
}
