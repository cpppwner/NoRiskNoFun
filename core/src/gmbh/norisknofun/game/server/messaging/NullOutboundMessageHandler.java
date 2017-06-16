package gmbh.norisknofun.game.server.messaging;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.server.OutboundMessageHandler;

/**
 * Special message handler used, when game wants to send outbound message directly
 * to one outbound handler, which is unknown.
 *
 * <p>
 *     Implementing the Null Object pattern.
 * </p>
 */
class NullOutboundMessageHandler implements OutboundMessageHandler {

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void handle(Message message) {
        // intentionally empty
    }
}
