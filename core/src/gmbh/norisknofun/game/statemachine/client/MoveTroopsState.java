package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.gamemessages.gui.MoveTroopGui;
import gmbh.norisknofun.game.networkmessages.Message;
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
    public void handleMessage(String senderId, Message message) {

        if(message.getType().equals(MoveTroop.class)){
            doMoveTroop((MoveTroop)message);
        }else if(message.getType().equals(MoveTroopCheck.class)){
            context.getGameData().setLastError(((MoveTroopCheck)message).getErrorMessage());
        }else if(message.getType().equals(NextPlayer.class)){
            setNextPlayer(((NextPlayer)message).getPlayername());

        }else if(message.getType().equals(MoveTroopGui.class)){
            requestMoveTroop((MoveTroopGui)message);
        }
        else {
            Gdx.app.log("WaitingForPlayers","unknown message"+message.getType().getSimpleName());
        }
    }

    private void requestMoveTroop(MoveTroopGui message){
        context.sendMessage(new MoveTroop(message.getFromRegion(),message.getToRegion(),message.getFigureId()));
    }
    private void setNextPlayer(String player){
        if(player!=null) {
            data.setCurrentPlayer(player);
            context.setState(new WaitingForNextTurnState(context));
        }
    }

    private void doMoveTroop(MoveTroop message) {
        context.getGameData().setGuiChanges(new MoveTroopGui(message.getFromRegion(),message.getToRegion(),message.getFigureId()));
    }
}
