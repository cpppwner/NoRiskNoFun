package gmbh.norisknofun.game.server.clients;

import java.util.UUID;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.server.OutboundMessageHandler;
import gmbh.norisknofun.network.Session;

/**
 * Created by cpppwner on 01.06.17.
 */
public class Client implements OutboundMessageHandler {

    private final String id;
    private final Session session;

    public Client(Session session) {
        id = UUID.randomUUID().toString();
        this.session = session;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void handle(Message message) {
        // TODO build up correct message and send it to the session
    }
}
