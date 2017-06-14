package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerAccepted;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerJoined;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerRejected;
import gmbh.norisknofun.game.statemachine.State;

/**
 * State used right after connection has been established to join to the game.
 */
class JoinGame extends State {

    private final ClientContext clientContext;

    JoinGame(ClientContext clientContext) {
        this.clientContext = clientContext;
    }

    @Override
    public void enter() {

        // send our join request to the server
        Message joinRequest = new PlayerJoined(clientContext.getGameData().getPlayerName());
        clientContext.sendMessage(joinRequest);
    }

    @Override
    public void handleMessage(String senderId, Message message) {

        if (message instanceof PlayerRejected) {
            handlePlayerRejected((PlayerRejected)message);
        } else if (message instanceof PlayerAccepted) {
            handlePlayerAccepted((PlayerAccepted)message);
        } else {
            Gdx.app.log(getClass().getSimpleName(), "Unsupported message class received " + message.getType().getName());
        }
    }

    private void handlePlayerAccepted(PlayerAccepted message) {

        // update myself (player) with all the information retrieved from server
        Player myself = clientContext.getGameData().getMyself();
        myself.setId(message.getPlayerId());
        myself.setColor(message.getPlayerColor());

        // update game related information
        clientContext.getGameData().setMapFilename(message.getMapName());
        clientContext.getGameData().setMaxNumPlayers(message.getMaxNumPlayers());

        // all data set, so let's make state transition
        clientContext.setState(new WaitingForPlayersState(clientContext));
    }

    private void handlePlayerRejected(PlayerRejected message) {

        // our join request was rejected by the server
        clientContext.getGameData().setLastError("Join rejected: " + message.getReason());
        clientContext.setState(new ErrorState());
    }
}
