package gmbh.norisknofun.game.server.messaging;

import java.util.LinkedList;
import java.util.List;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.server.InboundMessageHandler;
import gmbh.norisknofun.game.server.MessageBus;
import gmbh.norisknofun.game.server.OutboundMessageHandler;

/**
 * Implementation of the message bus interface.
 */
public final class MessageBusImpl implements MessageBus {

    /**
     * All registered {@link InboundMessageHandler inbound message handlers}.
     */
    private final List<InboundMessageHandler> inboundMessageHandlers;

    /**
     * All registered {@link OutboundMessageHandler outbound message handlers}.
     */
    private final List<OutboundMessageHandler> outboundMessageHandlers;

    /**
     * Default constructor.
     */
    public MessageBusImpl() {

        inboundMessageHandlers = new LinkedList<>();
        outboundMessageHandlers = new LinkedList<>();
    }

    @Override
    public void registerInboundMessageHandler(InboundMessageHandler handler) {

        if (handler == null)
            throw new IllegalArgumentException("handler is null");

        if (!inboundMessageHandlers.contains(handler)) {
            inboundMessageHandlers.add(handler);
        }
    }

    @Override
    public void unregisterInboundMessageHandler(InboundMessageHandler handler) {

        inboundMessageHandlers.remove(handler);
    }

    @Override
    public void registerOutboundMessageHandler(OutboundMessageHandler handler) {

        if (handler == null)
            throw new IllegalArgumentException("handler is null");

        if (!outboundMessageHandlers.contains(handler)) {
            outboundMessageHandlers.add(handler);
        }
    }

    @Override
    public void unregisterOutboundMessageHandler(OutboundMessageHandler handler) {

        outboundMessageHandlers.remove(handler);
    }

    @Override
    public void distributeInboundMessage(String senderId, Message message) {

        for (InboundMessageHandler handler : inboundMessageHandlers) {
            handler.handle(senderId, message);
        }
    }

    @Override
    public void distributeOutboundMessage(Message message) {

        for (OutboundMessageHandler handler : outboundMessageHandlers) {
            handler.handle(message);
        }
    }

    @Override
    public void distributeOutboundMessage(String recipientId, Message message) {

        OutboundMessageHandler handler = getOutboundMessageHandler(recipientId);
        handler.handle(message);
    }

    /**
     * Get the {@link OutboundMessageHandler} where {@link OutboundMessageHandler#getId()} is equal to given argument.
     *
     * @param recipientId The OutboundMessageHandler's id to search for.
     * @return The found {@link OutboundMessageHandler} or a special {@link NullOutboundMessageHandler}.
     */
    private OutboundMessageHandler getOutboundMessageHandler(String recipientId) {

        for (OutboundMessageHandler handler : outboundMessageHandlers) {
            if (handler.getId().equals(recipientId)) {
                return handler;
            }
        }

        return new NullOutboundMessageHandler();
    }

    /**
     * Get number of previously registered {@link InboundMessageHandler inbound message handlers}.
     *
     * <p>
     *     This method is intended for testing purposes only.
     * </p>
     *
     * @return Number of registered inbound message handlers.
     */
    public int getNumRegisteredInboundMessageHandlers() {

        return inboundMessageHandlers.size();
    }

    /**
     * Get number of previously registered {@link OutboundMessageHandler outbound message handlers}.
     *
     * <p>
     *     This method is intended for testing purposes only.
     * </p>
     *
     * @return Number of registered outbound message handlers.
     */
    public int getNumRegisteredOutboundMessageHandlers() {

        return outboundMessageHandlers.size();
    }
}
