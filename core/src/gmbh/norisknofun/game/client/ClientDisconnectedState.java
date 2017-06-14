package gmbh.norisknofun.game.client;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.network.Session;

/**
 * State used, when client is disconnected, but was previously connected.
 */
class ClientDisconnectedState implements ClientState {

    private final Client client;

    ClientDisconnectedState(Client client) {

        this.client = client;
    }

    @Override
    public void enter() {

        client.setSession(null);
        client.getMessageBuffer().clear();
    }

    @Override
    public void exit() {

    }

    @Override
    public void handleOutboundMessage(Message message) {

        // nothing to do - don't throw, maybe the game logic did not yet process our special message
        // see enter();
    }

    @Override
    public void handleNewSession(Session newSession) {

        throw new IllegalStateException("Not expecting new session here - huh :/");
    }

    @Override
    public void handleSessionClosed(Session closedSession) {

        // nothing to do here
    }

    @Override
    public void handleDataReceived() {

        // pending events, just clear out the message buffer
        client.getMessageBuffer().clear();
    }
}
