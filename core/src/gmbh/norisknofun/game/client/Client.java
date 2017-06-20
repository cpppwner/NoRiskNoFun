package gmbh.norisknofun.game.client;

import java.util.Queue;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.protocol.util.MessageBuffer;
import gmbh.norisknofun.network.Session;
import gmbh.norisknofun.network.SessionEventHandler;

/**
 * Client class used on client side to handle incoming data and outgoing messages.
 *
 * <p>
 *     Since a State pattern is implemented, this class is the "StateContext".
 * </p>
 */
public class Client implements OutboundMessageHandler, SessionEventHandler {

    /**
     * Client's current state.
     */
    private ClientState state;

    /**
     * Queue where incoming messages are added.
     */
    private final Queue<Message> inboundMessageQueue;

    /**
     * Buffer storing incoming byte data from network.
     */
    private final MessageBuffer messageBuffer;

    /**
     * The session for network communication;
     */
    private Session session;

    /**
     * Constructor taking the inbound message queue.
     *
     * @param inboundMessageQueue Message queue storing all inbound messages.
     */
    public Client(Queue<Message> inboundMessageQueue) {

        this.inboundMessageQueue = inboundMessageQueue;
        messageBuffer = new MessageBuffer();

        setInitialState();
    }

    private void setInitialState() {

        state = new ClientConnectingState(this);
        state.enter();
    }

    @Override
    public void handle(Message message) {

        state.handleOutboundMessage(message);
    }

    @Override
    public void newSession(Session session) {

        state.handleNewSession(session);
    }

    @Override
    public void sessionClosed(Session session) {

        state.handleSessionClosed(session);
    }

    @Override
    public void sessionDataReceived(Session session) {

        messageBuffer.append(session.read());
        state.handleDataReceived();
    }

    @Override
    public void sessionDataWritten(Session session) {

        // not interested in that event - ignoring
    }

    void setState(ClientState nextState) {

        state.exit();
        state = nextState;
        state.enter();
    }

    ClientState getCurrentState() {

        return state;
    }

    Session getSession() {

        return session;
    }

    MessageBuffer getMessageBuffer() {

        return messageBuffer;
    }

    void setSession(Session session) {

        this.session = session;
    }

    void distributeInboundMessage(Message message) {

        this.inboundMessageQueue.add(message);
    }

    public void resetState() {

        setInitialState();
    }
}
