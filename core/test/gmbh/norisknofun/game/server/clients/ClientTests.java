package gmbh.norisknofun.game.server.clients;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.server.MessageBus;
import gmbh.norisknofun.network.Session;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link Client}.
 */
public class ClientTests {

    private Session sessionMock;
    private MessageBus messageBusMock;

    private ClientState stateMockOne;
    private ClientState stateMockTwo;

    private Message messageMock;

    @Before
    public void setUp() {

        sessionMock = mock(Session.class);
        messageBusMock = mock(MessageBus.class);

        stateMockOne = mock(ClientState.class);
        stateMockTwo = mock(ClientState.class);

        messageMock = mock(Message.class);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void constructingClientWithNullSessionThrowsException() {

        // given
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("session is null"));

        // when/then
        new Client(null, messageBusMock);
    }

    @Test
    public void constructingClientWithNullMessageBusThrowsException() {

        // given
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("messageBus is null"));

        // when/then
        new Client(sessionMock, null);
    }

    @Test
    public void newlyConstructedClientSetsFieldsAccordingly() {

        // given
        Client target = new Client(sessionMock, messageBusMock);

        // then
        assertThat(target.getId(), not(isEmptyOrNullString()));
        assertThat(target.getSession(), is(sameInstance(sessionMock)));
        assertThat(target.getMessageBus(), is(sameInstance(messageBusMock)));
        assertThat(target.getMessageBuffer(), is(notNullValue()));
        assertThat(target.getMessageBuffer().length(), is(0));
        assertThat(target.getCurrentState(), is(instanceOf(ClientAcceptedState.class)));
    }

    @Test
    public void newlyConstructedClientRegistersItselfToTheMessageBusImmediately() {

        // given
        Client target = new Client(sessionMock, messageBusMock);

        // then
        verify(messageBusMock, times(1)).registerOutboundMessageHandler(target);
        verifyZeroInteractions(sessionMock);
    }

    @Test
    public void transitionBetweenStatesCallsEnterExitMethodsAccordingly() {

        // given
        Client target = new Client(sessionMock, messageBusMock);

        // when performing transition to first state
        target.setState(stateMockOne);

        // then
        verify(stateMockOne, times(1)).enter();
        verifyNoMoreInteractions(stateMockOne);
        verifyZeroInteractions(stateMockTwo);

        // and when performing transition to second state
        target.setState(stateMockTwo);

        // then
        verify(stateMockOne, times(1)).enter();
        verify(stateMockOne, times(1)).exit();
        verify(stateMockTwo, times(1)).enter();
        verifyNoMoreInteractions(stateMockTwo);
    }

    @Test
    public void subscribeToMessageBusRegistersClient() {

        // given
        Client target = new Client(sessionMock, messageBusMock);

        // when
        target.subscribeToMessageBus();

        // then
        verify(messageBusMock, times(2)).registerOutboundMessageHandler(target);
        verifyNoMoreInteractions(messageBusMock);
    }

    @Test
    public void unsubscribeFromMessageBusRemovesClient() {

        // given
        Client target = new Client(sessionMock, messageBusMock);

        // when
        target.unsubscribeFromMessageBus();

        // then
        verify(messageBusMock, times(1)).registerOutboundMessageHandler(target);
        verify(messageBusMock, times(1)).unregisterOutboundMessageHandler(target);
        verifyNoMoreInteractions(messageBusMock);
    }

    @Test
    public void distributeMessageDistributesInboundMessageViaMessageBus() {

        // given
        Client target = new Client(sessionMock, messageBusMock);

        // when
        target.distributeInboundMessage(messageMock);

        // then
        verify(messageBusMock, times(1)).registerOutboundMessageHandler(target);
        verify(messageBusMock, times(1)).distributeInboundMessage(target.getId(), messageMock);
        verifyNoMoreInteractions(messageBusMock);
        verifyZeroInteractions(messageMock);
    }

    @Test
    public void handleMessageForwardsMessageToCurrentState() {

        // given
        Client target = new Client(sessionMock, messageBusMock);
        target.setState(stateMockOne);

        // when
        target.handle(messageMock);

        // then
        verify(stateMockOne, times(1)).handleOutboundMessage(messageMock);
    }

    @Test
    public void processDataReceivedAppendsDataToInternalBufferAndCallsCurrentState() {

        // given
        Client target = new Client(sessionMock, messageBusMock);
        target.setState(stateMockOne);

        // when
        target.processDataReceived(new byte[] { (byte)0x01 });

        // then
        assertThat(target.getMessageBuffer().length(), is(1));
        assertThat(target.getMessageBuffer().peek(1), is(equalTo(new byte[] { (byte)0x01 })));
        verify(stateMockOne, times(1)).processDataReceived();

        // and when appending more data
        target.processDataReceived(new byte[] { (byte)0x02 });

        // then
        assertThat(target.getMessageBuffer().length(), is(2));
        assertThat(target.getMessageBuffer().peek(2), is(equalTo(new byte[] { (byte)0x01, (byte)0x02 })));
        verify(stateMockOne, times(2)).processDataReceived();
    }

    @Test
    public void sessionClosedGetsForwardedAccordingly() {

        // given
        Client target = new Client(sessionMock, messageBusMock);
        target.setState(stateMockOne);

        // when
        target.sessionClosed();

        // then
        verify(stateMockOne, times(1)).sessionClosed();
    }
}
