package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.gamemessages.gui.MoveTroopGui;
import gmbh.norisknofun.game.gamemessages.gui.RemoveTroopGui;
import gmbh.norisknofun.game.gamemessages.gui.SpawnTroopGui;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.common.MoveTroop;
import gmbh.norisknofun.game.networkmessages.common.NextPlayer;
import gmbh.norisknofun.game.networkmessages.common.RemoveTroop;
import gmbh.norisknofun.game.networkmessages.common.SpawnTroop;
import gmbh.norisknofun.game.networkmessages.common.SpawnTroopCheck;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by Katharina on 19.05.2017.
 */

public class SpreadTroopsState extends State {

    private ClientContext context;

    public SpreadTroopsState(ClientContext context) {
        this.context = context;
    }

    @Override
    public void handleMessage(String senderId, Message message) {


        if (message.getType().equals(NextPlayer.class)) {
            setNextPlayer(((NextPlayer) message).getPlayername());
        } else if (message.getType().equals(SpawnTroop.class)) {
            System.out.println("client state message id:"+((SpawnTroop)message).getId());
            doSpawnTroop((SpawnTroop) message);
        } else if (message.getType().equals(SpawnTroopCheck.class)) {
            // todo open dialog with error message
        } else if (message.getType().equals(SpawnTroopGui.class)) {
            requestSpawn((SpawnTroopGui) message);
        } else if (message.getType().equals(MoveTroopGui.class)) {
            requestMove((MoveTroopGui) message);
        } else if (message.getType().equals(MoveTroop.class)) {
            MoveTroopGui moveTroopGui= new MoveTroopGui(((MoveTroop)message).getFromRegion(), ((MoveTroop)message).getToRegion(),((MoveTroop) message).getFigureId() );
            moveTroopGui.setFigureId(((MoveTroop) message).getFigureId());
            context.getGameData().setGuiChanges(moveTroopGui); //temporary
        } else if (message.getType().equals(RemoveTroopGui.class)) {
            RemoveTroop removeTroop = new RemoveTroop();
            removeTroop.setAmount(1);
            removeTroop.setRegion(((RemoveTroopGui)message).getRegionName());
            context.sendMessage(removeTroop);
        } else if (message.getType().equals(RemoveTroop.class)) {
            context.getGameData().setGuiChanges(new RemoveTroopGui(((RemoveTroop)message).getRegion(), 1));
        }
        else {
            Gdx.app.log("Client SpreadTroopsState", "unknown message:"+message.getClass().getSimpleName());
        }
    }


    private void requestMove(MoveTroopGui message) {

        context.sendMessage(new MoveTroop("P1", 1, message.getFromRegion(), message.getToRegion(),message.getFigureId() ));
    }

    private void requestSpawn(SpawnTroopGui message) {
        context.sendMessage(new SpawnTroop("P1", message.getRegionName()));
    }

    private void doSpawnTroop(SpawnTroop message) {
        context.getGameData().setGuiChanges(new SpawnTroopGui(message.getRegionname(), 0, 0,message.getId()));
    }

    private void setNextPlayer(String playername) {
        if (playername != null)
            context.getGameData().setCurrentPlayer(playername);
    }
}
