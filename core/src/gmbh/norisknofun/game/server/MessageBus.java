package gmbh.norisknofun.game.server;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * Message bus for distributing messages between game (state machine) and connected clients.
 */
public interface MessageBus {

    /**
     * Register message handler handling messages received from clients.
     *
     * <p>
     *     If the same {@link InboundMessageHandler} is already registered nothing is done.
     * </p>
     *
     * @param handler The {@link InboundMessageHandler} to register.
     */
    void registerInboundMessageHandler(InboundMessageHandler handler);

    /**
     * Remove previously registered message handler.
     *
     * <p>
     *     If the {@link InboundMessageHandler} is not registered nothing is done.
     * </p>
     *
     * @param handler The {@link InboundMessageHandler} to unregister.
     */
    void unregisterInboundMessageHandler(InboundMessageHandler handler);

    /**
     * Register message handler handling messages sent to a client.
     *
     * <p>
     *     If the same {@link OutboundMessageHandler} is already registered nothing is done.
     * </p>
     *
     * @param handler The {@link OutboundMessageHandler} to register.
     */
    void registerOutboundMessageHandler(OutboundMessageHandler handler);

    /**
     * Remove previously registered message handler.
     *
     * <p>
     *     If the {@link OutboundMessageHandler} is not registered nothing is done.
     * </p>
     *
     * @param handler The {@link OutboundMessageHandler} to unregister.
     */
    void unregisterOutboundMessageHandler(OutboundMessageHandler handler);

    /**
     * Distribute inbound message to all {@link InboundMessageHandler inbound message handlers}.
     *
     * @param senderId The sender's identifier.
     * @param message The message to distribute.
     */
    void distributeInboundMessage(String senderId, Message message);

    /**
     * Distribute outbound message to all {@link OutboundMessageHandler outbound message handlers}.
     *
     * @param message The message to distribute.
     */
    void distributeOutboundMessage(Message message);

    /**
     * Distribute outbound message to a specifid {@link OutboundMessageHandler outbound message handler}.
     *
     * @param recipientId The outbound handler id which shall handle this message.
     * @param message The message to distribute.
     */
    void distributeOutboundMessage(String recipientId, Message message);
}
