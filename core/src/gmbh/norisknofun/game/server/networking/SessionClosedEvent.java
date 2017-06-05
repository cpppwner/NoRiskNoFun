package gmbh.norisknofun.game.server.networking;

import gmbh.norisknofun.network.Session;

/**
 * Created by cpppwner on 25.05.17.
 */

public class SessionClosedEvent implements SessionEvent {

    private final Session session;

    SessionClosedEvent(Session session) {

        this.session = session;
    }

    @Override
    public Session getSession() {
        return session;
    }

    @Override
    public void process(SessionEventProcessor processor) {
        processor.process(this);
    }
}
