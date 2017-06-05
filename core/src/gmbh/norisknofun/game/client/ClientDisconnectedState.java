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
        // TODO stefan.eberl - 2017-05-05 add special message letting the game logic also know about
        // disconnect event
    }

    @Override
    public void exit() {

    }

    @Override
    public void handleOutboundMessage(Message message) {

        // nothing to do - don't throw, maybe the game logic did not yet process our special message
        // se enter();
    }

    @Override
    public void handleNewSession(Session newSession) {

        // client got reconnected again - make state transition
        client.setSession(newSession);
        client.setState(new ClientConnectedState(client));
    }

    @Override
    public void handleSessionClosed(Session closedSession) {

        throw new IllegalStateException("session is already closed - huh :/");
    }

    @Override
    public void handleDataReceived() {

        // pending events, just clear out the message buffer
        client.getMessageBuffer().clear();
    }
}
