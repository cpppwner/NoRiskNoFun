package gmbh.norisknofun.game.server.clients;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * State of a client when the connection has been closed.
 */
final class ClientClosedState implements ClientState {

    private final Client context;

    ClientClosedState(Client context) {

        this.context = context;
    }

    @Override
    public void enter() {

        context.getMessageBuffer().clear();
    }

    @Override
    public void exit() {

        // intentionally left empty - nothing to do here
    }

    @Override
    public void handleOutboundMessage(Message message) {

        throw new IllegalStateException("outbound message is not expected");
    }

    @Override
    public void processDataReceived() {

        // should not occur, but there might be pending messages in the queue
        // just clean up the internal buffer
        context.getMessageBuffer().clear();
    }

    @Override
    public void sessionClosed() {

        // nothing to do here since, we are already in closed state
    }
}
