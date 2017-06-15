package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.gamemessages.gui.StartGameClicked;
import gmbh.norisknofun.game.networkmessages.EndGame;
import gmbh.norisknofun.game.networkmessages.Message;
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
    public void handleMessage(String senderId, Message message) {

        if(message.getType().equals(StartGame.class)){

            startGame();

        } else if (message.getType().equals(StartGameClicked.class)) {
            context.sendMessage(new StartGame(true));
        }
        else{
            Gdx.app.log(getClass().getSimpleName(),"unknown message " + message.getType().getName());
        }
    }

    private void startGame() {
        context.setState(new SpreadTroopsState(context));
        SceneManager.getInstance().setActiveScene(SceneNames.GAME_SCENE);
    }


}
