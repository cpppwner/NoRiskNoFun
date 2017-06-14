package gmbh.norisknofun.game.statemachine.server;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.attack.loser.ContinueAttack;
import gmbh.norisknofun.game.networkmessages.common.MoveTroop;
import gmbh.norisknofun.game.networkmessages.distribution.AddTroops;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by pippp on 15.05.2017.
 */

public class AttackLoserState extends State {

    private ServerContext context;
    private final GameDataServer data;
    private AttackState state;
    public AttackLoserState(ServerContext context, AttackState state){
        this.context=context;
        this.data=context.getGameData();
        this.state= state;
    }


    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void handleMessage(String senderId, Message message) {

        if(message.getType().equals(ContinueAttack.class)){
            handleDecision(senderId,(ContinueAttack)message);
        }else{
            Gdx.app.log("AttackLoserState","unknown message");
        }
    }

    private void handleDecision(String senderId, ContinueAttack message){
        if(message.isDecision()){
            state.setState(new ChooseTroopAmountState(context,state));
        }else{
            data.getAttackerRegion().setTroops(data.getAttackerRegion().getTroops()+data.getAttackingTroops());
            context.sendMessage(new AddTroops(data.getAttackingTroops()),senderId); // add remaining troops of attack
            data.clearAttackData();
            context.setState(new MoveTroopsState(context));
        }
    }

}
