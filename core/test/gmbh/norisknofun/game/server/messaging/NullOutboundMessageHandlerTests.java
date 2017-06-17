package gmbh.norisknofun.game.server.messaging;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

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
