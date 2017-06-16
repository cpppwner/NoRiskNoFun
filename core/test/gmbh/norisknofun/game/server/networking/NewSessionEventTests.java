package gmbh.norisknofun.game.server.networking;

import org.junit.Before;
import org.junit.Test;

import gmbh.norisknofun.network.Session;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for testing {@link NewSessionEvent}.
 */
public class NewSessionEventTests {

    private Session mockSession;
    private SessionEventProcessor mockProcessor;

    @Before
    public void setUp() {

        mockSession = mock(Session.class);
        mockProcessor = mock(SessionEventProcessor.class);
    }

    @Test
    public void creatingNewSessionEventWithNullWorks() {

        // given
        NewSessionEvent target = new NewSessionEvent(null);

        // then
        assertThat(target.getSession(), is(nullValue()));
    }

    @Test
    public void creatingNewSessionEventWithNotNullSession() {

        // given
        NewSessionEvent target = new NewSessionEvent(mockSession);

        // then
        assertThat(target.getSession(), is(sameInstance(mockSession)));
    }

    @Test
    public void processCallsProcessorAccordingly() {

        // given
        NewSessionEvent target = new NewSessionEvent(mockSession);

        // when
        target.process(mockProcessor);

        // then
        verify(mockProcessor, times(1)).process(target);
        verifyNoMoreInteractions(mockProcessor);
    }
}
