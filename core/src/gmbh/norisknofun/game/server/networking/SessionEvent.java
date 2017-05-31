package gmbh.norisknofun.game.server.networking;

import gmbh.norisknofun.network.Session;

/**
 * Created by cpppwner on 25.05.17.
 */
public interface SessionEvent {

    Session getSession();

    void process(SessionEventProcessor processor);
}
