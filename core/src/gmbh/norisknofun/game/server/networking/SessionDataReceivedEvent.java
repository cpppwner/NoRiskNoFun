package gmbh.norisknofun.game.server.networking;

import gmbh.norisknofun.network.Session;

/**
 * Session event to indicate that a session received new data over the network.
 */
public class SessionDataReceivedEvent implements SessionEvent {

    /** The session that received data from the network */
    private final Session session;

    /**
     * Initialize the event with the session that received the data.
     */
    SessionDataReceivedEvent(Session session) {

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
