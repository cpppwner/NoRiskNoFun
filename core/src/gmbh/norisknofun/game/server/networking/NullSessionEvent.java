package gmbh.norisknofun.game.server.networking;

import gmbh.norisknofun.network.Session;

/**
 * Special session event.
 *
 * <p>
 *     This class implements Null Object pattern.
 * </p>
 */
class NullSessionEvent implements SessionEvent {

    @Override
    public Session getSession() {
        return null;
    }

    @Override
    public void process(SessionEventProcessor processor) {
        // do nothing
    }
}
