package gmbh.norisknofun.game.client;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.network.Session;

/**
 * Base class for all client states.
 */
abstract class ClientStateBase implements ClientState {

    /**
     * The client - or StateContext in terms of state pattern
     */
    private final Client client;

    /**
     * Initialize the base class with the state context.
     */
    ClientStateBase(Client client) {

        this.client = client;
    }

    @Override
    public void enter() {
        // nothing to do here
    }

    @Override
    public void exit() {
        // nothing to do here
    }

    @Override
    public void handleOutboundMessage(Message message) {

        throw new IllegalStateException("unexpected handleOutboundMessage");
    }

    @Override
    public void handleNewSession(Session newSession) {

        throw new IllegalStateException("unexpected handleNewSession");
    }

    @Override
    public void handleSessionClosed(Session closedSession) {

        throw new IllegalStateException("unexpected handleSessionClosed");
    }

    @Override
    public void handleDataReceived() {

        throw new IllegalStateException("unexpected handleDataReceived");
    }

    Client getClient() {
        return client;
    }

    void terminateSession() {
        client.getSession().terminate();
    }

    void closeSession() {
        client.getSession().close();
    }

    void setNextState(ClientState nextState) {
        client.setState(nextState);
    }

    void setSession(Session newSession) {
        client.setSession(newSession);
    }

    void write(byte[] data) {

        client.getSession().write(data);
    }

    void distributeInboundMessage(Message inboundMessage) {

        client.distributeInboundMessage(inboundMessage);
    }

    void clearMessageBuffer() {
        client.getMessageBuffer().clear();
    }
}
