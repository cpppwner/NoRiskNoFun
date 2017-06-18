package gmbh.norisknofun.game.server.messaging;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.server.InboundMessageHandler;
import gmbh.norisknofun.game.server.OutboundMessageHandler;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Unit tests for testing {@link MessageBusImpl}.
 */
public class MessageBusImplTests {

    private InboundMessageHandler inboundHandlerOne;
    private InboundMessageHandler inboundHandlerTwo;

    private OutboundMessageHandler outboundHandlerOne;
    private OutboundMessageHandler outboundHandlerTwo;

    private static final String OUTBOUND_HANDLER_ONE_ID = "1";
    private static final String OUTBOUND_HANDLER_TWO_ID = "2";


    @Before
    public void setUp() {

        inboundHandlerOne = mock(InboundMessageHandler.class);
        inboundHandlerTwo = mock(InboundMessageHandler.class);

        outboundHandlerOne = mock(OutboundMessageHandler.class);
        outboundHandlerTwo = mock(OutboundMessageHandler.class);

        doReturn(OUTBOUND_HANDLER_ONE_ID).when(outboundHandlerOne).getId();
        doReturn(OUTBOUND_HANDLER_TWO_ID).when(outboundHandlerTwo).getId();
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void aDefaultConstructedMessageBusHasNoRegisteredHandlers() {

        // given
        MessageBusImpl target = new MessageBusImpl();

        // then
        assertThat(target.getNumRegisteredInboundMessageHandlers(), is(0));
        assertThat(target.getNumRegisteredOutboundMessageHandlers(), is(0));
    }

    @Test
    public void registerInboundMessageHandlerWithNullArgumentThrowsException() {

        // given
        MessageBusImpl target = new MessageBusImpl();

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("handler is null"));

        // when registering null, then exception is thrown
        target.registerInboundMessageHandler(null);
    }

    @Test
    public void registerInboundMessageHandlerAddsArgumentsToInternalCollection() {

        // given
        MessageBusImpl target = new MessageBusImpl();

        // when registering first handler
        target.registerInboundMessageHandler(inboundHandlerOne);

        // then
        assertThat(target.getNumRegisteredInboundMessageHandlers(), is(1));
        verifyZeroInteractions(inboundHandlerOne);

        // and when registering second handler
        target.registerInboundMessageHandler(inboundHandlerTwo);

        // then
        assertThat(target.getNumRegisteredInboundMessageHandlers(), is(2));
        verifyZeroInteractions(inboundHandlerOne, inboundHandlerTwo);
    }

    @Test
    public void registerInboundHandlerWithSameInstanceOnlyAddsItOnce() {

        // given
        MessageBusImpl target = new MessageBusImpl();

        // when adding the first time
        target.registerInboundMessageHandler(inboundHandlerOne);

        // then
        assertThat(target.getNumRegisteredInboundMessageHandlers(), is(1));

        // and when registering the same instance again
        target.registerInboundMessageHandler(inboundHandlerOne);

        // then
        assertThat(target.getNumRegisteredInboundMessageHandlers(), is(1));
    }

    @Test
    public void unregisterInboundHandlerWithNullArgument() {

        // given
        MessageBusImpl target = new MessageBusImpl();
        target.registerInboundMessageHandler(inboundHandlerOne);
        target.registerInboundMessageHandler(inboundHandlerTwo);

        // when unregister null
        target.unregisterInboundMessageHandler(null);

        // then
        assertThat(target.getNumRegisteredInboundMessageHandlers(), is(2));
    }

    @Test
    public void unregisterNotRegisteredInboundHandler() {

        // given
        MessageBusImpl target = new MessageBusImpl();
        target.registerInboundMessageHandler(inboundHandlerOne);

        // when unregister not registered handler
        target.unregisterInboundMessageHandler(inboundHandlerTwo);

        // then
        assertThat(target.getNumRegisteredInboundMessageHandlers(), is(1));
    }

    @Test
    public void unregisterInboundMessageHandlerRemovesFromInternalStructure() {

        // given
        MessageBusImpl target = new MessageBusImpl();
        target.registerInboundMessageHandler(inboundHandlerOne);
        target.registerInboundMessageHandler(inboundHandlerTwo);

        // when unregister first
        target.unregisterInboundMessageHandler(inboundHandlerOne);

        // then
        assertThat(target.getNumRegisteredInboundMessageHandlers(), is(1));

        // when unregister first
        target.unregisterInboundMessageHandler(inboundHandlerTwo);

        // then
        assertThat(target.getNumRegisteredInboundMessageHandlers(), is(0));
    }

    @Test
    public void registerOutboundMessageHandlerWithNullArgumentThrowsException() {

        // given
        MessageBusImpl target = new MessageBusImpl();

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("handler is null"));

        // when registering null, then exception is thrown
        target.registerOutboundMessageHandler(null);
    }

    @Test
    public void registerOutboundMessageHandlerAddsArgumentsToInternalCollection() {

        // given
        MessageBusImpl target = new MessageBusImpl();

        // when registering first handler
        target.registerOutboundMessageHandler(outboundHandlerOne);

        // then
        assertThat(target.getNumRegisteredOutboundMessageHandlers(), is(1));
        verifyZeroInteractions(outboundHandlerOne);

        // and when registering second handler
        target.registerOutboundMessageHandler(outboundHandlerTwo);

        // then
        assertThat(target.getNumRegisteredOutboundMessageHandlers(), is(2));
        verifyZeroInteractions(outboundHandlerOne, outboundHandlerTwo);
    }

    @Test
    public void registerOutboundHandlerWithSameInstanceOnlyAddsItOnce() {

        // given
        MessageBusImpl target = new MessageBusImpl();

        // when adding the first time
        target.registerOutboundMessageHandler(outboundHandlerOne);

        // then
        assertThat(target.getNumRegisteredOutboundMessageHandlers(), is(1));

        // and when registering the same instance again
        target.registerOutboundMessageHandler(outboundHandlerOne);

        // then
        assertThat(target.getNumRegisteredOutboundMessageHandlers(), is(1));
    }

    @Test
    public void unregisterOutboundHandlerWithNullArgument() {

        // given
        MessageBusImpl target = new MessageBusImpl();
        target.registerOutboundMessageHandler(outboundHandlerOne);
        target.registerOutboundMessageHandler(outboundHandlerTwo);

        // when unregister null
        target.unregisterOutboundMessageHandler(null);

        // then
        assertThat(target.getNumRegisteredOutboundMessageHandlers(), is(2));
    }

    @Test
    public void unregisterNotRegisteredOutboundHandler() {

        // given
        MessageBusImpl target = new MessageBusImpl();
        target.registerOutboundMessageHandler(outboundHandlerOne);

        // when unregister not registered handler
        target.unregisterOutboundMessageHandler(outboundHandlerTwo);

        // then
        assertThat(target.getNumRegisteredOutboundMessageHandlers(), is(1));
    }

    @Test
    public void unregisterOutboundMessageHandlerRemovesFromInternalStructure() {

        // given
        MessageBusImpl target = new MessageBusImpl();
        target.registerOutboundMessageHandler(outboundHandlerOne);
        target.registerOutboundMessageHandler(outboundHandlerTwo);

        // when unregister first
        target.unregisterOutboundMessageHandler(outboundHandlerOne);

        // then
        assertThat(target.getNumRegisteredOutboundMessageHandlers(), is(1));

        // when unregister first
        target.unregisterOutboundMessageHandler(outboundHandlerTwo);

        // then
        assertThat(target.getNumRegisteredOutboundMessageHandlers(), is(0));
    }

    @Test
    public void distributeInboundMessageForwardsMessageToAllRegisteredHandlers() {

        // given
        Message message = mock(Message.class);

        MessageBusImpl target = new MessageBusImpl();

        // when no inbound handlers are registered
        target.distributeInboundMessage("senderId", message);

        // then
        verifyZeroInteractions(message, inboundHandlerOne, inboundHandlerTwo);

        // when inbound handler one gets registered
        target.registerInboundMessageHandler(inboundHandlerOne);
        target.distributeInboundMessage("senderId", message);

        // then
        verifyZeroInteractions(message, inboundHandlerTwo);
        verify(inboundHandlerOne, times(1)).handle("senderId", message);

        // when inbound handler two gets also registered
        target.registerInboundMessageHandler(inboundHandlerTwo);
        target.distributeInboundMessage("senderId", message);

        // then
        verifyZeroInteractions(message);
        verify(inboundHandlerOne, times(2)).handle("senderId", message);
        verify(inboundHandlerTwo, times(1)).handle("senderId", message);
    }

    @Test
    public void distributeOutboundMessageSendsMessageToAllRegisteredOutboundHandlers() {

        // given
        Message message = mock(Message.class);

        MessageBusImpl target = new MessageBusImpl();

        // when no inbound handlers are registered
        target.distributeOutboundMessage(message);

        // then
        verifyZeroInteractions(message, outboundHandlerOne, outboundHandlerTwo);

        // when inbound handler one gets registered
        target.registerOutboundMessageHandler(outboundHandlerOne);
        target.distributeOutboundMessage(message);

        // then
        verifyZeroInteractions(message, outboundHandlerTwo);
        verify(outboundHandlerOne, times(1)).handle(message);

        // when inbound handler two gets also registered
        target.registerOutboundMessageHandler(outboundHandlerTwo);
        target.distributeOutboundMessage(message);

        // then
        verifyZeroInteractions(message);
        verify(outboundHandlerOne, times(2)).handle(message);
        verify(outboundHandlerTwo, times(1)).handle(message);
    }

    @Test
    public void distributeOutboundMessageCanBeSentToASingleHandler() {

        // given
        Message message = mock(Message.class);

        MessageBusImpl target = new MessageBusImpl();
        target.registerOutboundMessageHandler(outboundHandlerOne);
        target.registerOutboundMessageHandler(outboundHandlerTwo);

        // when
        target.distributeOutboundMessage(OUTBOUND_HANDLER_ONE_ID, message);

        // then
        verifyZeroInteractions(message);
        verify(outboundHandlerOne, times(1)).handle(message);
        verifyZeroInteractions(outboundHandlerTwo);

        // and when
        target.distributeOutboundMessage(OUTBOUND_HANDLER_TWO_ID, message);

        // then
        verifyZeroInteractions(message);
        verify(outboundHandlerOne, times(1)).handle(message);
        verify(outboundHandlerTwo, times(1)).handle(message);

        // and when
        target.distributeOutboundMessage("DoesNotExist", message);

        // then
        verifyZeroInteractions(message);
        verify(outboundHandlerOne, times(1)).handle(message);
        verify(outboundHandlerTwo, times(1)).handle(message);
    }
}
