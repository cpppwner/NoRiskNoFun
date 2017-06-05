package gmbh.norisknofun.game.server.networking;

import gmbh.norisknofun.network.Session;

/**
 * Created by cpppwner on 25.05.17.
 */

public class NullSessionEvent implements SessionEvent {

    @Override
    public Session getSession() {
        return null;
    }

    @Override
    public void process(SessionEventProcessor processor) {
        // do nothing
    }
}
