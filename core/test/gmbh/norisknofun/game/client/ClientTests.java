package gmbh.norisknofun.game.client;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.network.Session;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Unit tests for testing {@link Client} class.
 */
public class ClientTests {

    private ClientState mockStateOne;
    private ClientState mockStateTwo;
    private Message mockMessage;
    private Session mockSession;

    @Before
    public void setUp() {

        mockStateOne = mock(ClientState.class);
        mockStateTwo = mock(ClientState.class);
        mockMessage = mock(Message.class);
        mockSession = mock(Session.class);
    }

    @Test
    public void aDefaultConstructedClientInitializesCertainFields() {

        // given
        Client target = new Client(new LinkedList<Message>());

        // then
        assertThat(target.getMessageBuffer(), is(notNullValue()));
        assertThat(target.getSession(), is(nullValue()));
        assertThat(target.getCurrentState(), is(instanceOf(ClientConnectingState.class)));
    }

    @Test
    public void stateTransitionCallsEnterAndExitAccordingly() {

        // given
        Client target = new Client(new LinkedList<Message>());

        // when making state transition to first mock state
        target.setState(mockStateOne);

        // then
        verify(mockStateOne, times(1)).enter();
        verifyNoMoreInteractions(mockStateOne);
        verifyZeroInteractions(mockStateTwo);

        // and when making transition to second mock state
        target.setState(mockStateTwo);

        // then
        // then
        verify(mockStateOne, times(1)).enter();
        verify(mockStateOne, times(1)).exit();
        verify(mockStateTwo, times(1)).enter();
        verifyNoMoreInteractions(mockStateOne, mockStateTwo);
    }

    @Test
    public void handleForwardsMessageToState() {

        // given
        Client target = new Client(new LinkedList<Message>());
        target.setState(mockStateOne);

        // when
        target.handle(mockMessage);

        // then
        verify(mockStateOne, times(1)).enter();
        verify(mockStateOne, times(1)).handleOutboundMessage(mockMessage);
        verifyNoMoreInteractions(mockStateOne);
    }

    @Test
    public void newSessionIsForwardedToState() {

        // given
        Client target = new Client(new LinkedList<Message>());
        target.setState(mockStateOne);

        // when
        target.newSession(mockSession);

        // then
        verify(mockStateOne, times(1)).enter();
        verify(mockStateOne, times(1)).handleNewSession(mockSession);
        verifyNoMoreInteractions(mockStateOne);
    }

    @Test
    public void sessionClosedIsForwardedToState() {

        // given
        Client target = new Client(new LinkedList<Message>());
        target.setState(mockStateOne);

        // when
        target.sessionClosed(mockSession);

        // then
        verify(mockStateOne, times(1)).enter();
        verify(mockStateOne, times(1)).handleSessionClosed(mockSession);
        verifyNoMoreInteractions(mockStateOne);
    }

    @Test
    public void sessionDataReceivedReadsDataFromSessionAndDelegatesToState() {

        // given
        doReturn(new byte[] {(byte)0x12, (byte)0x34}).when(mockSession).read();

        Client target = new Client(new LinkedList<Message>());
        target.setState(mockStateOne);

        // when
        target.sessionDataReceived(mockSession);

        // then
        assertThat(target.getMessageBuffer().length(), is(2));
        assertThat(target.getMessageBuffer().peek(2), is(equalTo(new byte[] {(byte)0x12, (byte)0x34})));

        verify(mockSession, times(1)).read();
        verify(mockStateOne, times(1)).handleDataReceived();
        verify(mockStateOne, times(1)).enter();
        verifyNoMoreInteractions(mockSession, mockStateOne);
    }

    @Test
    public void sessionDataWrittenDoesNothingWithSessionOrState() {

        // given
        Client target = new Client(new LinkedList<Message>());
        target.setState(mockStateOne);

        // when
        target.sessionDataWritten(mockSession);

        // then
        verify(mockStateOne, times(1)).enter();
        verifyNoMoreInteractions(mockStateOne);
        verifyZeroInteractions(mockSession);
    }

    @Test
    public void distributeInboundMessageAddsMessageToQueue() {

        // given
        LinkedList<Message> messageQueue = new LinkedList<>();
        Client target = new Client(messageQueue);
        target.setState(mockStateOne);

        // when distributing first message
        target.distributeInboundMessage(mockMessage);

        // then
        assertThat(messageQueue, is(equalTo(Collections.singletonList(mockMessage))));

        // and when distributing a second message
        target.distributeInboundMessage(mockMessage);

        // then
        assertThat(messageQueue, is(equalTo(Arrays.asList(mockMessage, mockMessage))));
    }

    @Test
    public void setGetSession() {

        // given
        Client target = new Client(new LinkedList<Message>());

        // when
        target.setSession(mockSession);

        // then
        assertThat(target.getSession(), is(sameInstance(mockSession)));
    }
}
