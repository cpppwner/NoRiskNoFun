package gmbh.norisknofun.game.statemachine.client;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.gamemessages.client.ClientConnected;
import gmbh.norisknofun.game.gamemessages.client.ClientConnectionRefused;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Initial state, waiting until the client has successfully connected to the server.
 */
class ConnectingState extends State {

    /**
     * Context in terms of the state pattern.
     */
    private final ClientContext clientContext;

    ConnectingState(ClientContext clientContext) {

        this.clientContext = clientContext;
    }

    @Override
    public void handleMessage(String senderId, Message message) {

        if (message instanceof ClientConnected) {
            // client successfully connected to server - make transition to next state
            clientContext.setState(new JoinGame(clientContext));

        } else if (message instanceof ClientConnectionRefused) {
            // connection was refused by server - bail out to main menu and set the error before
            handleConnectionRefused((ClientConnectionRefused)message);
        } else {
            Gdx.app.log(getClass().getSimpleName(), "Unsupported message class received " + message.getType().getName());
        }
    }

    /**
     * Connection was refused by server - this is before we try to join to the game.
     */
    private void handleConnectionRefused(ClientConnectionRefused message) {

        clientContext.getGameData().setLastError("Connection refused: " + message.getReason());
        clientContext.setState(new ErrorState());
    }
}
