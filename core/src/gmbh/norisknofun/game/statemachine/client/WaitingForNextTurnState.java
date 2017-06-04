package gmbh.norisknofun.game.statemachine.client;



import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.networkmessages.common.NextPlayer;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by Katharina on 31.05.2017.
 */

public class WaitingForNextTurnState extends State {

    private ClientContext context;
    private final GameData data;

    public WaitingForNextTurnState(ClientContext context){
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
    public void handleMessage(String senderId, BasicMessageImpl message) {
        if(message.getType().equals(NextPlayer.class)){
            setNextPlayer(((NextPlayer)message).getPlayername());
        }else{
            Gdx.app.log("WaitingForNextTurnState","unknown messgae");
        }
    }


    private void setNextPlayer(String playername){
        if(playername!=null){
            data.setCurrentplayer(playername);
            //todo check if nextplayer name is your own player name and change state
        }
    }
}
