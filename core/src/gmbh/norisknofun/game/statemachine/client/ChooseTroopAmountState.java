package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.gamemessages.gui.ChooseTroopsAmountGui;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.attack.choosetroops.ChooseTroopsAmount;
import gmbh.norisknofun.game.networkmessages.attack.choosetroops.ChooseTroopsAmountCheck;
import gmbh.norisknofun.game.statemachine.State;
import gmbh.norisknofun.scene.SceneManager;
import gmbh.norisknofun.scene.SceneNames;

/**
 * Created by Katharina on 19.05.2017.
 */

public class ChooseTroopAmountState extends State {

    private ClientContext context;
    private AttackState attackState;
    public ChooseTroopAmountState(ClientContext context, AttackState state){
        this.context=context;
        this.attackState =state;
        SceneManager.getInstance().setActiveScene(SceneNames.TROOP_AMOUNT_SCENE);
    }


    @Override
    public void handleMessage(String senderId, Message message) {
        Gdx.app.log("Client ChooseTroopAmount", "Handling message: " + message.getClass().getName());
        if(message.getType().equals(ChooseTroopsAmountCheck.class)){
           handleChooseTroopAmountCheckMessage((ChooseTroopsAmountCheck)message);
        }else if(message.getType().equals(ChooseTroopsAmountGui.class)){
            requestTroopAmount((ChooseTroopsAmountGui) message);
        }else{
            Gdx.app.log("Client - ChooseTroopAmountState","unknown message:"+message.getClass().getSimpleName());
        }
    }


    private void handleChooseTroopAmountCheckMessage(ChooseTroopsAmountCheck message){
        Gdx.app.log("Client ChooseTroopAmount", "Received Check: " + message.isCheck());

        if(message.isCheck()){
            attackState.setState(new EvaluateDiceResultState(context, attackState));
        }else{
            context.getGameData().setLastError(message.getErrormessage());
        }
    }

    private void requestTroopAmount(ChooseTroopsAmountGui message){
        context.sendMessage(new ChooseTroopsAmount(message.getAmount()));
    }


}
