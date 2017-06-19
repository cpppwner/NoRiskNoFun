package gmbh.norisknofun.game.server.networking;

/**
 * Interface for processing the session events.
 */
public interface SessionEventProcessor {

    /**
     * Process new session event.
     *
     * @param event {@link NewSessionEvent}
     */
    void process(NewSessionEvent event);

    /**
     * Process session closed event.
     *
     * @param event {@link SessionClosedEvent}
     */
    void process(SessionClosedEvent event);

    /**
     * Process data received event.
     *
     * @param event {@link SessionDataReceivedEvent}
     */
    void process(SessionDataReceivedEvent event);
}
