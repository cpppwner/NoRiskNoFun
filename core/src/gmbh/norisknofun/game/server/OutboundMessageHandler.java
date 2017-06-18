package gmbh.norisknofun.game.server;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * Message handler dealing with messages sent from the server to the client (outbound).
 */
public interface OutboundMessageHandler {

    /**
     * Get unique identifier of this handler.
     *
     * @return Unique identifier.
     */
    String getId();

    /**
     * Handle a message.
     *
     * @param message Message to handle.
     */
    void handle(Message message);
}
