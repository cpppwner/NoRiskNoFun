package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.gamemessages.gui.SpawnTroopGui;
import gmbh.norisknofun.game.gamemessages.gui.UpdateCurrentPlayerGui;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.common.NextPlayer;
import gmbh.norisknofun.game.networkmessages.common.SpawnTroop;
import gmbh.norisknofun.game.networkmessages.common.SpawnTroopCheck;
import gmbh.norisknofun.game.networkmessages.spread.PlayerSpreadFinished;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by Katharina on 19.05.2017.
 */

public class SpreadTroopsState extends State {

    private final ClientContext context;

    public SpreadTroopsState(ClientContext context) {
        this.context = context;
    }

    @Override
    public void handleMessage(String senderId, Message message) {


        if (message.getType().equals(NextPlayer.class)) {
            setNextPlayer(((NextPlayer) message).getPlayername());
        } else if (message.getType().equals(SpawnTroop.class)) {
            doSpawnTroop((SpawnTroop) message);
        } else if (message.getType().equals(SpawnTroopCheck.class)) {
            handleSpawnError((SpawnTroopCheck) message);
        } else if (message.getType().equals(SpawnTroopGui.class)) {
            requestSpawn((SpawnTroopGui) message);
        } else if (message.getType().equals(PlayerSpreadFinished.class)) {
            stateTransition((PlayerSpreadFinished) message);
        }
        else {
            Gdx.app.log("Client SpreadTroopsState", "unknown message:"+message.getClass().getSimpleName());
        }
    }

    /**
     * Transition to Distribution State if it's your turn,
     * otherwise go to Waiting State and wait for your turn
     */
    private void stateTransition(PlayerSpreadFinished message) {
        //context.getGameData().setCurrentPlayer(message.getCurrentPlayerName());
        context.getGameData().setGuiChanges(new UpdateCurrentPlayerGui(message.getCurrentPlayerName()));
        Gdx.app.log("SpreadTroop Transition", "Current Player: " + context.getGameData().getCurrentPlayer().getPlayerName() + " Myself: " + context.getGameData().getMyself().getPlayerName());

        if (context.getGameData().getMyself().getPlayerName().equals(message.getCurrentPlayerName())) {
            context.setState(new DistributionState(context));
        } else {
            context.setState(new WaitingForNextTurnState(context));
        }
    }


    private void requestSpawn(SpawnTroopGui message) {
        context.sendMessage(new SpawnTroop(message.getRegionName(), message.getId()));
    }

    private void doSpawnTroop(SpawnTroop message) {
        context.getGameData().setGuiChanges(new SpawnTroopGui(message.getRegionname(), message.getId()));
    }

    private void setNextPlayer(String playername) {
        if (playername != null)
            //context.getGameData().setCurrentPlayer(playername);
            // do not update the current player directly in the state machine.
            // use the GameScene rendering thread to prevent race conditions
            context.getGameData().setGuiChanges(new UpdateCurrentPlayerGui(playername));
        Gdx.app.log("Client SpreadTroopState", "Next Player: " + playername);
    }

    private void handleSpawnError(SpawnTroopCheck message) {
        context.getGameData().setLastError(message.getErrormessage());
    }
}
