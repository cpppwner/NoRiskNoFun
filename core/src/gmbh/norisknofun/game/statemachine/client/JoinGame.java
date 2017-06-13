package gmbh.norisknofun.game.statemachine.client;

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

        Message joinRequest = new PlayerJoined(clientContext.getGameData().getPlayerName());
        clientContext.sendMessage(joinRequest);
    }

    @Override
    public void handleMessage(String senderId, Message message) {

        if (message instanceof PlayerRejected) {
            handlePlayerRejected((PlayerRejected)message);
        } else if (message instanceof PlayerAccepted) {
            handlePlayerAccepted((PlayerAccepted)message);
        }

    }

    private void handlePlayerAccepted(PlayerAccepted message) {

    }

    private void handlePlayerRejected(PlayerRejected message) {
    }
}
