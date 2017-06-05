package gmbh.norisknofun.game.server.clients;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * State interface describing the State of a {@link Client}.
 */
interface ClientState {

    /**
     * Called upon state transition when this state becomes the new one.
     */
    void enter();

    /**
     * Called upon state transition when this state was the old one.
     */
    void exit();

    /**
     * Called when the game logic wants to send a {@link Message} to the client.
     *
     * @param message The message to send to the client.
     */
    void handleOutboundMessage(Message message);

    /**
     * Called when new data was received from the client.
     */
    void processDataReceived();

    /**
     * Called when session was closed.
     */
    void sessionClosed();
}
