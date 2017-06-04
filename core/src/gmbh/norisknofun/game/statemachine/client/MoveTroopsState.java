package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.networkmessages.common.MoveTroop;
import gmbh.norisknofun.game.networkmessages.common.MoveTroopCheck;
import gmbh.norisknofun.game.networkmessages.common.NextPlayer;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by Katharina on 19.05.2017.
 */

public class MoveTroopsState extends State {

    private ClientContext context;
    private final GameData data;

    public MoveTroopsState(ClientContext context){
        this.context=context;
        data=this.context.getGameData();
    }
    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void handleMessage(String senderId,BasicMessageImpl message) {

        if(message.getType().equals(MoveTroop.class)){
            // todo interface between statemachine and GUI
        }else if(message.getType().equals(MoveTroopCheck.class)){
            // todo interface between statemachine and GUI
        }else if(message.getType().equals(NextPlayer.class)){
            setNextPlayer(((NextPlayer)message).getPlayername());

        }
        else {
            Gdx.app.log("WaitingForPlayers","unknown message");
        }
    }

    private void setNextPlayer(String player){
        if(player!=null) {
            data.setCurrentplayer(player);
            context.setState(new WaitingForNextTurnState(context));
        }
    }
}
