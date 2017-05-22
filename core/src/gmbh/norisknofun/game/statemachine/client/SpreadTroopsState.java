package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.networkmessages.ChangeState;
import gmbh.norisknofun.game.networkmessages.EndGame;
import gmbh.norisknofun.game.networkmessages.common.MoveTroop;
import gmbh.norisknofun.game.networkmessages.common.NextPlayer;
import gmbh.norisknofun.game.networkmessages.common.SpawnTroop;
import gmbh.norisknofun.game.networkmessages.common.SpawnTroopCheck;
import gmbh.norisknofun.game.statemachine.State;
import gmbh.norisknofun.scene.SceneManager;
import gmbh.norisknofun.scene.SceneNames;

/**
 * Created by Katharina on 19.05.2017.
 */

public class SpreadTroopsState implements State {

    private ClientContext context;

    public SpreadTroopsState(ClientContext context){
        this.context=context;
    }
    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void handleMessage(BasicMessageImpl message) {


        if(message.getType().equals(ChangeState.class)){
            context.setState(((ChangeState) message).state);
        }else if(message.getType().equals(NextPlayer.class)){
            setNextPlayer(((NextPlayer)message).playername);
        }else if(message.getType().equals(SpawnTroop.class)){
                // todo interface between statemachine and GUI
        }else if(message.getType().equals(SpawnTroopCheck.class)){
            // todo open dialog with error message
        }
        else {
            Gdx.app.log("WaitingForPlayers","unknown message");
        }
    }


    private void setNextPlayer(String playername){
       context.getGameData().setCurrentplayer(playername);
    }
}
