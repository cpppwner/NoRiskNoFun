package gmbh.norisknofun.game.server.networking;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Tests for testing {@link NewSessionEvent}.
 */
public class NullSessionEventTests {

    private SessionEventProcessor mockProcessor;

    @Before
    public void setUp() {

        mockProcessor = mock(SessionEventProcessor.class);
    }

    @Test
    public void getSessionAlwaysReturnsNull() {

        // given
        NullSessionEvent target = new NullSessionEvent();

        // then
        assertThat(target.getSession(), is(nullValue()));
    }

    @Test
    public void processDoesNotInvokeProcessor() {

        // given
        NullSessionEvent target = new NullSessionEvent();

        // when
        target.process(mockProcessor);

        // then
        verifyZeroInteractions(mockProcessor);
    }
}
