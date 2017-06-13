package gmbh.norisknofun.game.statemachine.client;

import gmbh.norisknofun.game.gamemessages.client.ClientConnected;
import gmbh.norisknofun.game.gamemessages.client.ClientConnectionRefused;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Initial state, waiting until the client has successfully connected to the server.
 */
class ConnectingState extends State {

    private final ClientContext clientContext;

    ConnectingState(ClientContext clientContext) {

        this.clientContext = clientContext;
    }

    @Override
    public void handleMessage(String senderId, Message message) {

        if (message instanceof ClientConnected) {
            // client successfully connected to server - make transition to next state


        } else if (message instanceof ClientConnectionRefused) {
            // connection was refused by server - bail out to main menu and set the error before

        }
    }
}
