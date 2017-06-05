package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.networkmessages.EndGame;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.StartGame;
import gmbh.norisknofun.game.statemachine.State;
import gmbh.norisknofun.scene.SceneManager;
import gmbh.norisknofun.scene.SceneNames;

/**
 * Created by Katharina on 19.05.2017.
 */

public class WaitingForPlayersState extends State {
    private ClientContext context;
    public WaitingForPlayersState( ClientContext context){
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

        if(message.getType().equals(EndGame.class)){
            SceneManager.getInstance().setActiveScene(SceneNames.MAIN_MENU_SCENE);
        }else if(message.getType().equals(StartGame.class)){
            context.setState(new SpreadTroopsState(context));
        }
        else{
            Gdx.app.log("WaitingForPlayers","unknown message");
        }
    }


}
