package gmbh.norisknofun.game.client;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.network.Session;

/**
 * Initial state, when a client is starting to connect.
 */
class ClientConnectingState implements ClientState {

    private final Client client;

    ClientConnectingState(Client client) {
        this.client = client;
    }

    @Override
    public void enter() {
        // nothing to do
    }

    @Override
    public void exit() {
        // nothing to do
    }

    @Override
    public void handleOutboundMessage(Message message) {

        throw new IllegalStateException("cannot handle outbound message");
    }

    @Override
    public void handleNewSession(Session newSession) {

        client.setSession(newSession);
        client.setState(new ClientConnectedState(client));
    }

    @Override
    public void handleSessionClosed(Session closedSession) {

        throw new IllegalStateException("unexpected session closed event");
    }

    @Override
    public void handleDataReceived() {

        throw new IllegalStateException("unexpected data received event");
    }
}
