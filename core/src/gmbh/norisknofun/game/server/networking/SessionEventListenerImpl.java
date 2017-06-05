package gmbh.norisknofun.game.server.networking;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import gmbh.norisknofun.network.Session;

/**
 * Created by cpppwner on 25.05.17.
 */

public class SessionEventListenerImpl implements SessionEventListener {

    private final BlockingQueue<SessionEvent> sessionEvents = new LinkedBlockingQueue<>();

    @Override
    public void newSession(Session session) {

        sessionEvents.add(new NewSessionEvent(session));
    }

    @Override
    public void sessionClosed(Session session) {

        sessionEvents.add(new SessionClosedEvent(session));
    }

    @Override
    public void sessionDataReceived(Session session) {

        sessionEvents.add(new SessionDataReceivedEvent(session));
    }

    @Override
    public void sessionDataWritten(Session session) {

        // not interested in that event
        // therefore intentionally left empty
    }

    @Override
    public SessionEvent pollSessionEvent(long timeout, TimeUnit timeUnit) throws InterruptedException {

        SessionEvent event = sessionEvents.poll(timeout, timeUnit);

        return event != null ? event : new NullSessionEvent();
    }
}
