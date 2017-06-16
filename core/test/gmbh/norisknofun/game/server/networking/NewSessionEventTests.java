package gmbh.norisknofun.game.server.networking;

import org.junit.Before;
import org.junit.Test;

import gmbh.norisknofun.network.Session;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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
