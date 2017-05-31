package gmbh.norisknofun.game.server.messaging;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.server.InboundMessageHandler;
import gmbh.norisknofun.game.server.MessageBus;
import gmbh.norisknofun.game.server.OutboundMessageHandler;

/**
 * Created by cpppwner on 01.06.17.
 */

public class MessageBusImpl implements MessageBus {
    @Override
    public void registerInboundMessageHandler(InboundMessageHandler handler) {

    }

    @Override
    public void unregisterInboundMessageHandler(InboundMessageHandler handler) {

    }

    @Override
    public void registerOutboundMessageHandler(OutboundMessageHandler handler) {

    }

    @Override
    public void unregsiterOutboundMessageHandler(OutboundMessageHandler handler) {

    }

    @Override
    public void distributeInboundMessage(String senderId, Message message) {

    }

    @Override
    public void distributeOutboundMessage(Message message) {

    }

    @Override
    public void distributeOutboundMessage(String recipientId, Message message) {

    }
}
