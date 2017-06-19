package gmbh.norisknofun.game.client;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * Interface used on Client side to send messages to server.
 */
public interface OutboundMessageHandler {

    /**
     * Handle outbound message.
     *
     * @param  message
     */
    void handle(Message message);
}
