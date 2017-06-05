package gmbh.norisknofun.game.server.messaging;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.server.OutboundMessageHandler;

/**
 * Created by cpppwner on 01.06.17.
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
