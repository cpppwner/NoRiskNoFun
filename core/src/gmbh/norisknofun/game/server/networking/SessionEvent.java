package gmbh.norisknofun.game.server.networking;

import gmbh.norisknofun.network.Session;

/**
 * Common interface for all session events.
 *
 * <p>
 *     This is just encapsulating things client/server send.
 *     Note, i know the server/client should send such a class instead of calling 4 different methods
 *     but due to lack of time i won't refactor this.
 * </p>
 */
public interface SessionEvent {

    /**
     * Get session that caused the event.
     */
    Session getSession();

    /**
     * Process the event.
     * @param processor The processor who is called with the correct subclass.
     */
    void process(SessionEventProcessor processor);
}
