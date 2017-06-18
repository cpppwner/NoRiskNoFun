package gmbh.norisknofun.game.server.clients;

import java.util.UUID;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.protocol.util.MessageBuffer;
import gmbh.norisknofun.game.server.MessageBus;
import gmbh.norisknofun.game.server.OutboundMessageHandler;
import gmbh.norisknofun.network.Session;

/**
 * Class used to handle a connected client.
 */
public final class Client implements OutboundMessageHandler {

    /**
     * Client's unique identifier.
     */
    private final String id;

    /**
     * The session for network communication.
     */
    private final Session session;

    /**
     * The message bus to distribute inbound messages and receive outbound messsages.
     */
    private final MessageBus messageBus;

    /**
     * Internal buffer storing data read from the socket.
     */
    private final MessageBuffer messageBuffer;

    /**
     * The client's current state.
     */
    private ClientState state;

    /**
     * Constructor taking all necessary arguments.
     * @param session Client's session for network communication.
     * @param messageBus The message bus for distributing/receiving messages.
     */
    public Client(Session session, MessageBus messageBus) {

        if (session == null)
            throw new IllegalArgumentException("session is null");
        if (messageBus == null)
            throw new IllegalArgumentException("messageBus is null");

        id = UUID.randomUUID().toString();
        this.session = session;
        this.messageBus = messageBus;
        messageBuffer = new MessageBuffer();

        setInitialState();
    }

    /**
     * Set client's initial state.
     */
    private void setInitialState() {

        state = new ClientConnectedState(this);
        state.enter();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void handle(Message message) {

        state.handleOutboundMessage(message);
    }

    /**
     * Called when new data is avaible from socket.
     * @param data The read data.
     */
    void processDataReceived(byte[] data) {

        messageBuffer.append(data);
        state.processDataReceived();
    }

    /**
     * Called when session received close event or GameServer needs to be stopped
     */
    public void sessionClosed() {
        state.sessionClosed();
    }

    /**
     * Change the client's state.
     *
     * <p>
     *     Internal method, which only an implementing {@link ClientState} is allowed to call.
     *     The current's state {@link ClientState#exit()} is called and the new state's
     *     {@link ClientState#enter()} during transition.
     * </p>
     *
     * @param nextState The new state to set.
     */
    void setState(ClientState nextState) {
        state.exit();
        state = nextState;
        state.enter();
    }

    /**
     * Get the message buffer.
     *
     * <p>
     *     Only implementing {@link ClientState} is allowed to call this.
     * </p>
     */
    MessageBuffer getMessageBuffer() {

        return messageBuffer;
    }

    /**
     * Get the session.
     *
     * <p>
     *     Only implementing {@link ClientState} is allowed to call this.
     * </p>
     */
    Session getSession() {

        return session;
    }

    /**
     * Get {@link MessageBus} passed in constructor.
     */
    MessageBus getMessageBus() {

        return messageBus;
    }

    /**
     * Subscribe (register) this client with the {@link MessageBus}.
     *
     * <p>
     *     Only implementing {@link ClientState} is allowed to call this.
     * </p>
     */
    void subscribeToMessageBus() {

        messageBus.registerOutboundMessageHandler(this);
    }

    /**
     * Unsubscribe (deregister) this client from the {@link MessageBus}.
     *
     * <p>
     *     Only implementing {@link ClientState} is allowed to call this.
     * </p>
     */
    void unsubscribeFromMessageBus() {

        messageBus.unregisterOutboundMessageHandler(this);
    }

    /**
     * Distribute a {@link Message} via the {@link MessageBus}.
     *
     * <p>
     *     Only implementing {@link ClientState} is allowed to call this.
     * </p>
     *
     * @param message The message to distribute.
     */
    void distributeInboundMessage(Message message) {

        messageBus.distributeInboundMessage(getId(), message);
    }

    /**
     * Get the client's current state.
     *
     * <p>
     *     This is intended for testing purposes only and therefore package internal.
     * </p>
     *
     * @return Client's current state.
     */
    ClientState getCurrentState() {

        return state;
    }
}
