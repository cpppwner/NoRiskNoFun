package gmbh.norisknofun.game.server.clients;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.server.MessageBus;
import gmbh.norisknofun.network.Session;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Integration tests for testing the {@link ClientClosedState} class.
 */
public class ClientClosedStateTests {

    private Session sessionMock;
    private MessageBus messageBusMock;
    private ClientState clientStateMock;
    private Client client;

    @Before
    public void setUp() {

        sessionMock = mock(Session.class);
        messageBusMock = mock(MessageBus.class);
        client = new Client(sessionMock, messageBusMock);
        clientStateMock = mock(ClientState.class);
        client.setState(clientStateMock); // just to have another state
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void enteringTheStateClearsMessageBuffer() {

        // given
        client.processDataReceived(new byte[]{(byte)0x01, (byte)0x02});
        ClientClosedState target = new ClientClosedState(client);

        // verify that there is data in the buffer
        assertThat(client.getMessageBuffer().length(), is(2));

        // when entering the ClientClosedState
        target.enter();

        // then data is cleared
        assertThat(client.getMessageBuffer().length(), is(0));
    }

    @Test
    public void exitingStateDoesNothing() {

        // given
        client.processDataReceived(new byte[]{(byte)0x01, (byte)0x02});
        ClientClosedState target = new ClientClosedState(client);

        // when
        target.exit();

        // then
        assertThat(client.getMessageBuffer().length(), is(2));
        assertThat(client.getCurrentState(), is(sameInstance(clientStateMock)));
        verify(clientStateMock, times(1)).enter();
        verify(clientStateMock, times(1)).processDataReceived();
        verifyNoMoreInteractions(clientStateMock, messageBusMock);
        verifyZeroInteractions(sessionMock, messageBusMock);
    }

    @Test
    public void handleOutboundMessageThrowsException() {

        // given
        ClientClosedState target = new ClientClosedState(client);

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage(is("outbound message is not expected"));

        // when/then
        target.handleOutboundMessage(mock(Message.class));
    }

    @Test
    public void processDataReceivedClearsMessageBuffer() {

        // given
        client.processDataReceived(new byte[]{(byte)0x01, (byte)0x02});
        ClientClosedState target = new ClientClosedState(client);

        // verify that the message buffer contains data
        assertThat(client.getMessageBuffer().length(), is(2));

        // when
        target.processDataReceived();

        // then
        assertThat(client.getMessageBuffer().length(), is(0));
        assertThat(client.getCurrentState(), is(sameInstance(clientStateMock)));
        verify(clientStateMock, times(1)).enter();
        verify(clientStateMock, times(1)).processDataReceived();
        verifyNoMoreInteractions(clientStateMock, messageBusMock);
        verifyZeroInteractions(sessionMock, messageBusMock);
    }

    @Test
    public void sessionClosedDoesNothing() {

        // given
        client.processDataReceived(new byte[]{(byte)0x01, (byte)0x02});
        ClientClosedState target = new ClientClosedState(client);

        // when
        target.sessionClosed();

        // then
        assertThat(client.getMessageBuffer().length(), is(2));
        assertThat(client.getCurrentState(), is(sameInstance(clientStateMock)));
        verify(clientStateMock, times(1)).enter();
        verify(clientStateMock, times(1)).processDataReceived();
        verifyNoMoreInteractions(clientStateMock, messageBusMock);
        verifyZeroInteractions(sessionMock, messageBusMock);
    }
}
