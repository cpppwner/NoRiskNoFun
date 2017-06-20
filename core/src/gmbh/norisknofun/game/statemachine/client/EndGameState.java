package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.gamemessages.gui.EndGameGui;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.statemachine.State;
import gmbh.norisknofun.game.statemachine.server.ServerContext;
import gmbh.norisknofun.scene.SceneManager;
import gmbh.norisknofun.scene.SceneNames;

/**
 * Created by pippp on 18.06.2017.
 */

public class EndGameState extends State {


    private final ServerContext context;
    private final GameDataServer data;


    public EndGameState(ServerContext context){
        this.context=context;
        this.data=this.context.getGameData();
    }


    @Override
    public void handleMessage(String senderId, Message message) {

        if(message.getType().equals(EndGameGui.class)){
            SceneManager.getInstance().setActiveScene(SceneNames.MAIN_MENU_SCENE);
        }else {
            Gdx.app.log("EndGameState","unknown Message:"+message.getType().getSimpleName());
        }
    }


}
