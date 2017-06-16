package gmbh.norisknofun.game.server.networking;

import gmbh.norisknofun.network.Session;

/**
 * Session event that is used on the game server side when a new session is established.
 *
 * <p>
 *     This happens if a new connection was established on the NetworkServer.
 * </p>
 */
public class NewSessionEvent implements SessionEvent {

    /** Session that was newly created */
    private final Session session;

    /**
     * Initialize the event with the session.
     */
    NewSessionEvent(Session session) {

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
