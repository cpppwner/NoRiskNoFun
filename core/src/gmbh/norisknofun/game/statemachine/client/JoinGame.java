package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerAccepted;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerJoined;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerRejected;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by cpppwner on 13.06.17.
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

    }

    private void handlePlayerRejected(PlayerRejected message) {

        // our join request was rejected by the server
        clientContext.getGameData().setLastError("Join rejected: " + message.getReason());
        clientContext.setState(new ErrorState());
    }
}
