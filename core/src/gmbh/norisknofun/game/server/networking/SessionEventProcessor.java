package gmbh.norisknofun.game.server.networking;

/**
 * Created by cpppwner on 25.05.17.
 */

public interface SessionEventProcessor {

    void process(NewSessionEvent event);

    void process(SessionClosedEvent event);

    void process(SessionDataReceivedEvent event);
}
