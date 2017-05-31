package gmbh.norisknofun.network;

/**
 * Event handler invoked when something happens with the session.
 *
 * <p>
 *     Note: all methods are called within the Context of the NetworkServer thread.
 *     The implementing class must ensure the thread safety.
 * </p>
 */
public interface SessionEventHandler {

    /**
     * Called when a new session was created.
     *
     * @param session Newly created session.
     */
    void newSession(Session session);

    /**
     * Called when a session was closed.
     *
     * @param session Session that got closed.
     */
    void sessionClosed(Session session);

    /**
     * Called when a session received data.
     *
     * <p>
     *     This method is invoked after the data became available in the session.
     * </p>
     *
     * @param session Session that has data available.
     */
    void sessionDataReceived(Session session);

    /**
     * Called when data for that session was written to the socket.
     *
     * <p>
     *     It's not mandatory that all data was written.
     * </p>
     *
     * @param session The session for which data was written to the socket.
     */
    void sessionDataWritten(Session session);
}
