package gmbh.norisknofun.game.server.networking;

import gmbh.norisknofun.network.Session;

/**
 * Special event used when a session was closed.
 */
public class SessionClosedEvent implements SessionEvent {

    /** Session that got closed */
    private final Session session;

    /**
     * Initialize the event with session that got closed.
     */
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
