package gmbh.norisknofun.game.server.messaging;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit test for testing {@link NullOutboundMessageHandler}.
 */
public class NullOutboundMessageHandlerTests {

    @Test
    public void getIdGivesNull() {

        // given
        NullOutboundMessageHandler target = new NullOutboundMessageHandler();

        // when
        String obtained = target.getId();

        // then
        assertThat(obtained, is(nullValue()));
    }
}
