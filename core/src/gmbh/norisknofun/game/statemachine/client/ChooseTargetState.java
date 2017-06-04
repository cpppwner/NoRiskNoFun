package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.networkmessages.choosetarget.AttackRegionCheck;
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
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void handleMessage(String senderId, BasicMessageImpl message) {

       if(message.getType().equals(AttackRegionCheck.class)){
            //todo show dialog with error message
        }else {
            Gdx.app.log("WaitingForPlayers","unknown message");
        }
    }
}
