package gmbh.norisknofun.game.client;

import gmbh.norisknofun.game.gamemessages.client.ClientDisconnected;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.network.Session;

/**
 * State used, when client is disconnected, but was previously connected.
 */
class ClientDisconnectedState extends ClientStateBase {

    ClientDisconnectedState(Client client) {

        super(client);
    }

    @Override
    public void enter() {

        distributeInboundMessage(new ClientDisconnected());
        setSession(null);
        clearMessageBuffer();
    }

    @Override
    public void handleOutboundMessage(Message message) {

        // nothing to do - don't throw, maybe the game logic did not yet process our special message
        // see enter();
    }

    @Override
    public void handleSessionClosed(Session closedSession) {

        // nothing to do here
    }

    @Override
    public void handleDataReceived() {

        // pending events, just clear out the message buffer
        clearMessageBuffer();
    }
}
