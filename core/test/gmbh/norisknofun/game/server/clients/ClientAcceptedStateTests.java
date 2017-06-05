package gmbh.norisknofun.game.server.clients;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import gmbh.norisknofun.GdxTest;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.protocol.MessageSerializer;
import gmbh.norisknofun.game.protocol.ProtocolException;
import gmbh.norisknofun.game.server.MessageBus;
import gmbh.norisknofun.network.Session;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Integration tests for testing {@link ClientAcceptedState} class.
 */
public class ClientAcceptedStateTests extends GdxTest {

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
    public void enteringTheStateSubscribesClientToMessageBus() {

        // given
        ClientAcceptedState target = new ClientAcceptedState(client);

        // when
        target.enter();

        // then
        verify(messageBusMock, times(2)).registerOutboundMessageHandler(client);
        verify(messageBusMock, times(1)).unregisterOutboundMessageHandler(client);
        verifyNoMoreInteractions(messageBusMock);
        verifyZeroInteractions(messageBusMock);
    }

    @Test
    public void exitingStateDoesNothing() {

        // given
        ClientAcceptedState target = new ClientAcceptedState(client);

        // when
        target.exit();

        // then
        verify(messageBusMock, times(1)).registerOutboundMessageHandler(client);
        verify(messageBusMock, times(2)).unregisterOutboundMessageHandler(client);
        verifyNoMoreInteractions(messageBusMock);
        verifyZeroInteractions(messageBusMock);
    }

    @Test
    public void handleOutboundMessageSerializesMessageAndWritesDataToSession() throws IOException, ProtocolException {

        // given
        ClientAcceptedState target = new ClientAcceptedState(client);

        final List<byte[]> observedData = new LinkedList<>();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                observedData.add((byte[]) invocation.getArgument(0));
                return null;
            }
        }).when(sessionMock).write(ArgumentMatchers.<byte[]>any());

        // when
        target.handleOutboundMessage(new TestMessage());

        // then
        assertThat(observedData.size(), is(1));
        assertThat(observedData.get(0), is(notNullValue()));

        byte[] expectedMessage = new MessageSerializer(new TestMessage()).serialize();
        assertThat(observedData.get(0), is(equalTo(expectedMessage)));
    }

    @Test
    public void whenSerializationFailsSessionGetsTerminated() {

        // given
        ClientAcceptedState target = new ClientAcceptedState(client);

        // when
        target.handleOutboundMessage(new NotSerializableMessage());

        // then
        assertThat(client.getCurrentState(), is(instanceOf(ClientClosedState.class)));
        verify(sessionMock, times(1)).terminate();
        verifyNoMoreInteractions(sessionMock);
    }

    @Test
    public void processDataReceivedDoesNothingWhenThereIsNoMessageToDeserialize() {

        // given
        client.processDataReceived(new byte[] { (byte)0x00});
        ClientAcceptedState target = new ClientAcceptedState(client);

        // when
        target.processDataReceived();

        // then
        assertThat(client.getMessageBuffer().length(), is(1));
        assertThat(client.getCurrentState(), is(sameInstance(clientStateMock)));
        verifyZeroInteractions(sessionMock);
        verify(messageBusMock, times(1)).registerOutboundMessageHandler(client);
        verify(messageBusMock, times(1)).unregisterOutboundMessageHandler(client);
        verifyNoMoreInteractions(messageBusMock);
    }

    @Test
    public void processDataReceivedTerminatesClientIfDeserializationFails() throws IOException, ProtocolException {

        // given
        byte[] data = new MessageSerializer(new TestMessage()).serialize();
        for (int i = 3; i < data.length; i++) {
            data[i] = (byte)0x7F; // overwrite message type identifier & payload
        }
        client.processDataReceived(data);
        ClientAcceptedState target = new ClientAcceptedState(client);

        // when
        target.processDataReceived();

        // then
        assertThat(client.getCurrentState(), is(instanceOf(ClientClosedState.class)));
        verify(sessionMock, times(1)).terminate();
        verifyNoMoreInteractions(sessionMock);
        verify(messageBusMock, times(1)).registerOutboundMessageHandler(client);
        verify(messageBusMock, times(1)).unregisterOutboundMessageHandler(client);
        verifyNoMoreInteractions(messageBusMock);
    }

    @Test
    public void processDataReceivedDistributesMessageViaMessageBus() throws IOException, ProtocolException {

        byte[] data = new MessageSerializer(new TestMessage()).serialize();
        client.processDataReceived(data);
        ClientAcceptedState target = new ClientAcceptedState(client);

        // when
        target.processDataReceived();

        // then
        assertThat(client.getMessageBuffer().length(), is(0));
        assertThat(client.getCurrentState(), is(sameInstance(clientStateMock)));
        verify(messageBusMock, times(1)).distributeInboundMessage(eq(client.getId()), any(TestMessage.class));
        verify(messageBusMock, times(1)).registerOutboundMessageHandler(client);
        verify(messageBusMock, times(1)).unregisterOutboundMessageHandler(client);
        verifyNoMoreInteractions(messageBusMock);
        verifyZeroInteractions(sessionMock);
    }

    @Test
    public void sessionClosedMakesStateTransitionToClientClosedState() {

        // given
        ClientAcceptedState target = new ClientAcceptedState(client);

        // when
        target.sessionClosed();

        // then
        assertThat(client.getCurrentState(), is(instanceOf(ClientClosedState.class)));
        verifyZeroInteractions(sessionMock);
        verify(messageBusMock, times(1)).registerOutboundMessageHandler(client);
        verify(messageBusMock, times(1)).unregisterOutboundMessageHandler(client);
        verifyNoMoreInteractions(messageBusMock);
    }

    private static final class TestMessage implements Message, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public Class<? extends Message> getType() {
            return getClass();
        }
    }

    private static final class NotSerializableMessage implements Message {

        @Override
        public Class<? extends Message> getType() {
            return getClass();
        }
    }
}
