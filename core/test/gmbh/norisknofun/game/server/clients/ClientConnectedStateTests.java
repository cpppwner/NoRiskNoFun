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
import java.util.ArrayList;
import java.util.List;

import gmbh.norisknofun.GdxTest;
import gmbh.norisknofun.game.gamemessages.client.ClientConnected;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.protocol.MessageDeserializer;
import gmbh.norisknofun.game.protocol.MessageDeserializerTests;
import gmbh.norisknofun.game.protocol.MessageSerializer;
import gmbh.norisknofun.game.protocol.ProtocolConstants;
import gmbh.norisknofun.game.protocol.ProtocolException;
import gmbh.norisknofun.game.protocol.messages.handshake.Handshake;
import gmbh.norisknofun.game.protocol.messages.handshake.HandshakeAccepted;
import gmbh.norisknofun.game.protocol.messages.handshake.HandshakeConstants;
import gmbh.norisknofun.game.protocol.messages.handshake.HandshakeRejected;
import gmbh.norisknofun.game.protocol.util.MessageBuffer;
import gmbh.norisknofun.game.server.MessageBus;
import gmbh.norisknofun.game.server.networking.SessionClosedEvent;
import gmbh.norisknofun.network.Session;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for testing {@link ClientConnectedState}.
 */
public class ClientConnectedStateTests extends GdxTest {

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
    public void enteringTheStateDoeNothing() {

        // given
        client.processDataReceived(new byte[]{(byte)0x01, (byte)0x02});
        ClientConnectedState target = new ClientConnectedState(client);

        // when
        target.enter();

        // then
        assertThat(client.getMessageBuffer().length(), is(2));
        assertThat(client.getCurrentState(), is(sameInstance(clientStateMock)));
        verify(clientStateMock, times(1)).enter();
        verify(clientStateMock, times(1)).processDataReceived();
        verifyNoMoreInteractions(clientStateMock, messageBusMock);
        verifyZeroInteractions(sessionMock, messageBusMock);
    }

    @Test
    public void exitingStateDoesNothing() {

        // given
        client.processDataReceived(new byte[]{(byte)0x01, (byte)0x02});
        ClientConnectedState target = new ClientConnectedState(client);

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
        ClientConnectedState target = new ClientConnectedState(client);

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("no Messages can be handled in this state");

        // when
        target.handleOutboundMessage(mock(Message.class));
    }

    @Test
    public void processDataReceivedDoesNothingWhenThereIsNoMessageToDeserialize() {

        // given
        client.processDataReceived(new byte[] { (byte)0x00});
        ClientConnectedState target = new ClientConnectedState(client);

        // when
        target.processDataReceived();

        // then
        assertThat(client.getMessageBuffer().length(), is(1));
        assertThat(client.getCurrentState(), is(sameInstance(clientStateMock)));
        verifyZeroInteractions(sessionMock);
        verifyNoMoreInteractions(messageBusMock);
    }

    @Test
    public void processDataReceivedTerminatesClientIfDeserializationFails() throws IOException, ProtocolException {

        // given
        byte[] data = new MessageSerializer(new ClientConnectedStateTests.TestMessage()).serialize();
        for (int i = 3; i < data.length; i++) {
            data[i] = (byte)0x7F; // overwrite message type identifier & payload
        }
        client.processDataReceived(data);
        ClientConnectedState target = new ClientConnectedState(client);

        // when
        target.processDataReceived();

        // then
        assertThat(client.getCurrentState(), is(instanceOf(ClientClosedState.class)));
        verify(sessionMock, times(1)).terminate();
        verifyNoMoreInteractions(sessionMock);
        verifyNoMoreInteractions(messageBusMock);
    }

    @Test
    public void processDataReceivedTerminatesClientIfMessageIsNotHandshakeMessage() throws IOException, ProtocolException {

        // given
        byte[] data = new MessageSerializer(new ClientConnectedStateTests.TestMessage()).serialize();
        client.processDataReceived(data);
        ClientConnectedState target = new ClientConnectedState(client);

        // when
        target.processDataReceived();

        // then
        assertThat(client.getCurrentState(), is(instanceOf(ClientClosedState.class)));
        verify(sessionMock, times(1)).terminate();
        verifyNoMoreInteractions(sessionMock);
        verifyNoMoreInteractions(messageBusMock);
    }

    @Test
    public void processDataReceivedTerminatesClientIfClientSendsMoreThanHandshakeMessage() throws IOException, ProtocolException {

        // given

        // build up handshake message and add a single byte afterwards to the data array
        Handshake handshake = new Handshake(HandshakeConstants.HANDSHAKE_MAGIC, HandshakeConstants.HANDSHAKE_PROTOCOL_VERSION);
        byte[] handshakeData = new MessageSerializer(handshake).serialize();
        MessageBuffer buffer = new MessageBuffer();
        buffer.append(handshakeData);
        buffer.append(new byte[] {(byte)0x00});

        client.processDataReceived(buffer.read(buffer.length()));
        ClientConnectedState target = new ClientConnectedState(client);

        // when
        target.processDataReceived();

        // then
        assertThat(client.getCurrentState(), is(instanceOf(ClientClosedState.class)));
        verify(sessionMock, times(1)).terminate();
        verifyNoMoreInteractions(sessionMock);
        verifyNoMoreInteractions(messageBusMock);
    }

    @Test
    public void processDataReceivedTerminatesClientIfClientSendsWrongMagic() throws IOException, ProtocolException {

        // given
        Handshake handshake = new Handshake("foo", HandshakeConstants.HANDSHAKE_PROTOCOL_VERSION);
        byte[] handshakeData = new MessageSerializer(handshake).serialize();

        client.processDataReceived(handshakeData);
        ClientConnectedState target = new ClientConnectedState(client);

        // when
        target.processDataReceived();

        // then
        assertThat(client.getCurrentState(), is(instanceOf(ClientClosedState.class)));
        verify(sessionMock, times(1)).terminate();
        verifyNoMoreInteractions(sessionMock);
        verifyNoMoreInteractions(messageBusMock);
    }

    @Test
    public void processDataReceivedTerminatesSendingMessageToClientFails() throws IOException, ProtocolException {

        // given
        doThrow(IOException.class).when(sessionMock).write(ArgumentMatchers.any(byte[].class));

        Handshake handshake = new Handshake(HandshakeConstants.HANDSHAKE_MAGIC, HandshakeConstants.HANDSHAKE_PROTOCOL_VERSION);
        byte[] handshakeData = new MessageSerializer(handshake).serialize();

        client.processDataReceived(handshakeData);
        ClientConnectedState target = new ClientConnectedState(client);

        // when
        target.processDataReceived();

        // then
        assertThat(client.getCurrentState(), is(instanceOf(ClientClosedState.class)));
        verify(sessionMock, times(1)).terminate();
        verify(sessionMock, times(1)).write(ArgumentMatchers.any(byte[].class));
        verifyNoMoreInteractions(sessionMock);
        verifyNoMoreInteractions(messageBusMock);
    }

    @Test
    public void processDataReceivedRejectsClientWithDifferentProtocolVersion() throws IOException, ProtocolException {

        // given
        final List<byte[]> observedInWrite = new ArrayList<>();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                observedInWrite.add((byte[]) invocation.getArgument(0));
                return null;
            }
        }).when(sessionMock).write(ArgumentMatchers.any(byte[].class));

        Handshake handshake = new Handshake(HandshakeConstants.HANDSHAKE_MAGIC, "bar");
        byte[] handshakeData = new MessageSerializer(handshake).serialize();

        client.processDataReceived(handshakeData);
        ClientConnectedState target = new ClientConnectedState(client);

        // when
        target.processDataReceived();

        // then
        assertThat(client.getCurrentState(), is(instanceOf(ClientClosedState.class)));
        assertThat(observedInWrite.size(), is(1));

        // deserialize message and check contents
        MessageBuffer buffer = new MessageBuffer();
        buffer.append(observedInWrite.get(0));
        Message obtainedMessage = new MessageDeserializer(buffer).deserialize();
        assertThat(obtainedMessage, is(instanceOf(HandshakeRejected.class)));
        assertThat(((HandshakeRejected)obtainedMessage).getServerProtocolVersion(), is(HandshakeConstants.HANDSHAKE_PROTOCOL_VERSION));

        verify(sessionMock, times(1)).write(ArgumentMatchers.any(byte[].class));
        verify(sessionMock, times(1)).close();
        verifyNoMoreInteractions(sessionMock);
        verifyNoMoreInteractions(messageBusMock);
    }

    @Test
    public void processDataReceivedAcceptsClient() throws IOException, ProtocolException {

        // given
        final List<byte[]> observedInWrite = new ArrayList<>();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                observedInWrite.add((byte[]) invocation.getArgument(0));
                return null;
            }
        }).when(sessionMock).write(ArgumentMatchers.any(byte[].class));

        Handshake handshake = new Handshake(HandshakeConstants.HANDSHAKE_MAGIC, HandshakeConstants.HANDSHAKE_PROTOCOL_VERSION);
        byte[] handshakeData = new MessageSerializer(handshake).serialize();

        client.processDataReceived(handshakeData);
        ClientConnectedState target = new ClientConnectedState(client);

        // when
        target.processDataReceived();

        // then
        assertThat(client.getCurrentState(), is(instanceOf(ClientAcceptedState.class)));
        assertThat(observedInWrite.size(), is(1));

        // deserialize message and check contents
        MessageBuffer buffer = new MessageBuffer();
        buffer.append(observedInWrite.get(0));
        Message obtainedMessage = new MessageDeserializer(buffer).deserialize();
        assertThat(obtainedMessage, is(instanceOf(HandshakeAccepted.class)));
        assertThat(((HandshakeAccepted)obtainedMessage).getServerProtocolVersion(), is(HandshakeConstants.HANDSHAKE_PROTOCOL_VERSION));

        verify(sessionMock, times(1)).write(ArgumentMatchers.any(byte[].class));
        verify(messageBusMock, times(1)).registerOutboundMessageHandler(client);
        verify(messageBusMock, times(1)).distributeInboundMessage(eq(client.getId()), ArgumentMatchers.any(ClientConnected.class));
        verifyNoMoreInteractions(sessionMock, messageBusMock);
    }

    @Test
    public void sessionClosedSetsStateToSessionClosedState() {

        // given
        ClientConnectedState target = new ClientConnectedState(client);

        // when
        target.sessionClosed();

        // then
        assertThat(client.getCurrentState(), is(instanceOf(ClientClosedState.class)));
        verifyZeroInteractions(sessionMock, messageBusMock);

    }

    private static final class TestMessage implements Message, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public Class<? extends Message> getType() {
            return getClass();
        }
    }
}
