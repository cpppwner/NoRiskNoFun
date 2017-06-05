package gmbh.norisknofun.game.server;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * Message handler dealing with inbound messages.
 *
 * <p>
 *     Inbound messages are those which are sent by the client and have to be processed by the
 *     game server.
 * </p>
 */
public interface InboundMessageHandler {

    /**
     * Handle the message.
     *
     * @param senderId The sender id, which can also be used to identify {@link OutboundMessageHandler} in {@link MessageBus}.
     * @param message The message to handle.
     */
    void handle(String senderId, Message message);
}
