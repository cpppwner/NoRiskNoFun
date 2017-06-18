package gmbh.norisknofun.game.server.networking;

/**
 * Interface for processing the session events.
 */
public interface SessionEventProcessor {

    /**
     * Process new session event.
     */
    void process(NewSessionEvent event);

    /**
     * Process session closed event.
     */
    void process(SessionClosedEvent event);

    /**
     * Process data received event.
     */
    void process(SessionDataReceivedEvent event);
}
