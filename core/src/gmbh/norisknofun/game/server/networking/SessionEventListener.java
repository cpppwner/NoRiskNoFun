package gmbh.norisknofun.game.server.networking;

import java.util.concurrent.TimeUnit;

import gmbh.norisknofun.network.SessionEventHandler;

/**
 * Session event listener.
 *
 * <p>
 *     This is a link between the {@link SessionEventHandler} and the game server thread.
 *     The implementation must ensure that all session events are stored in a thread safe
 *     structure and that the game server can poll those events.
 * </p>
 */
public interface SessionEventListener extends SessionEventHandler {

    /**
     * Poll next pending session event.
     *
     * @param timeout The timeout for polling.
     * @param timeUnit The time unit for polling.
     * @return Next pending {@link SessionEvent} or {@link NullSessionEvent} if no other event is pending.
     * @throws InterruptedException In case the thread gets interrupted.
     */
    SessionEvent pollSessionEvent(long timeout, TimeUnit timeUnit) throws InterruptedException;
}
