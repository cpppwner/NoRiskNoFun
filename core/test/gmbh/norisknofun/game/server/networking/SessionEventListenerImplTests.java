package gmbh.norisknofun.game.server.networking;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import gmbh.norisknofun.network.Session;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Tests for testing {@link SessionEventListenerImpl}.
 */
public class SessionEventListenerImplTests {

    private Session mockSessionOne;
    private Session mockSessionTwo;

    @Before
    public void setUp() {

        mockSessionOne = mock(Session.class);
        mockSessionTwo = mock(Session.class);
    }

    @Test
    public void aDefaultConstructedSessionEventListenerHasNoPendingEvents() throws InterruptedException {

        // given
        SessionEventListenerImpl target = new SessionEventListenerImpl();

        // when
        SessionEvent obtained = target.pollSessionEvent(1, TimeUnit.MICROSECONDS);

        // then
        assertThat(obtained, is(instanceOf(NullSessionEvent.class)));
    }

    @Test
    public void newSessionAddsAppropriateSessionEvent() throws InterruptedException {

        // given
        SessionEventListenerImpl target = new SessionEventListenerImpl();

        // when
        target.newSession(mockSessionOne);
        SessionEvent obtained = target.pollSessionEvent(1, TimeUnit.MICROSECONDS);

        // then
        assertThat(obtained, is(instanceOf(NewSessionEvent.class)));
        assertThat(obtained.getSession(), is(sameInstance(mockSessionOne)));
    }

    @Test
    public void sessionClosedAddsAppropriateSessionEvent() throws InterruptedException {

        // given
        SessionEventListenerImpl target = new SessionEventListenerImpl();

        // when
        target.sessionClosed(mockSessionOne);
        SessionEvent obtained = target.pollSessionEvent(1, TimeUnit.MICROSECONDS);

        // then
        assertThat(obtained, is(instanceOf(SessionClosedEvent.class)));
        assertThat(obtained.getSession(), is(sameInstance(mockSessionOne)));
    }

    @Test
    public void sessionDataReceivedAddsAppropriateSessionEvent() throws InterruptedException {

        // given
        SessionEventListenerImpl target = new SessionEventListenerImpl();

        // when
        target.sessionDataReceived(mockSessionOne);
        SessionEvent obtained = target.pollSessionEvent(1, TimeUnit.MICROSECONDS);

        // then
        assertThat(obtained, is(instanceOf(SessionDataReceivedEvent.class)));
        assertThat(obtained.getSession(), is(sameInstance(mockSessionOne)));
    }

    @Test
    public void sessionDataWrittenDoesNotAddAnySessionEvent() throws InterruptedException {

        // given
        SessionEventListenerImpl target = new SessionEventListenerImpl();

        // when
        target.sessionDataWritten(mockSessionOne);
        SessionEvent obtained = target.pollSessionEvent(1, TimeUnit.MICROSECONDS);

        // then
        assertThat(obtained, is(instanceOf(NullSessionEvent.class)));
    }

    @Test
    public void pollingSessionEventsGivesThemInTheSameOrderAsTheyArrived() throws InterruptedException {

        // given
        SessionEventListenerImpl target = new SessionEventListenerImpl();
        target.newSession(mockSessionOne);
        target.sessionDataReceived(mockSessionOne);
        target.newSession(mockSessionTwo);
        target.sessionClosed(mockSessionOne);

        // when polling first event
        SessionEvent obtained = target.pollSessionEvent(1, TimeUnit.MICROSECONDS);

        // then new session one is expected
        assertThat(obtained, is(instanceOf(NewSessionEvent.class)));
        assertThat(obtained.getSession(), is(sameInstance(mockSessionOne)));

        // when polling second event
        obtained = target.pollSessionEvent(1, TimeUnit.MICROSECONDS);

        // then new session one is expected
        assertThat(obtained, is(instanceOf(SessionDataReceivedEvent.class)));
        assertThat(obtained.getSession(), is(sameInstance(mockSessionOne)));

        // when polling third event
        obtained = target.pollSessionEvent(1, TimeUnit.MICROSECONDS);

        // then new session one is expected
        assertThat(obtained, is(instanceOf(NewSessionEvent.class)));
        assertThat(obtained.getSession(), is(sameInstance(mockSessionTwo)));

        // when polling forth event
        obtained = target.pollSessionEvent(1, TimeUnit.MICROSECONDS);

        // then session closed event is expected
        assertThat(obtained, is(instanceOf(SessionClosedEvent.class)));
        assertThat(obtained.getSession(), is(sameInstance(mockSessionOne)));

        // and when polling once more, although no event is pending
        obtained = target.pollSessionEvent(1, TimeUnit.MICROSECONDS);

        // then session closed event is expected
        assertThat(obtained, is(instanceOf(NullSessionEvent.class)));
    }
}
