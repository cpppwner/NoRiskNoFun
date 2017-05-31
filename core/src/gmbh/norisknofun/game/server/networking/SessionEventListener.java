package gmbh.norisknofun.game.server.networking;

import java.util.concurrent.TimeUnit;

import gmbh.norisknofun.network.SessionEventHandler;

/**
 * Implementation of session event listener.
 */
public interface SessionEventListener extends SessionEventHandler {

    SessionEvent pollSessionEvent(long timeout, TimeUnit timeUnit) throws InterruptedException;
}
