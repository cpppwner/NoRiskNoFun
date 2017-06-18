package gmbh.norisknofun.game.client;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import gmbh.norisknofun.GdxTest;
import gmbh.norisknofun.game.gamemessages.client.ClientDisconnected;
import gmbh.norisknofun.game.gamemessages.client.DisconnectClient;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.protocol.MessageDeserializer;
import gmbh.norisknofun.game.protocol.MessageSerializer;
import gmbh.norisknofun.game.protocol.ProtocolException;
import gmbh.norisknofun.game.protocol.util.MessageBuffer;
import gmbh.norisknofun.network.Session;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Tests for testing the {@link ClientConnectedState}.
 */
public class ClientConnectedStateTests extends GdxTest {

    private LinkedList<Message> messageQueue;
    private Client client;
    private Session mockSession;
    private ClientState mockState;

    @Before
    public void setUp() {

        mockSession = mock(Session.class);
        mockState = mock(ClientState.class);

        messageQueue = new LinkedList<>();
        client = new Client(messageQueue);
        client.setSession(mockSession);
        client.setState(mockState);
    }

    @Test
    public void enterDoesNothing() {

        // given
        client.getMessageBuffer().append(new byte[] { (byte)0x01, (byte)0x02 });
        ClientConnectedState target = new ClientConnectedState(client);

        // when
        target.enter();

        // then
        assertThat(client.getMessageBuffer().length(), is(2));
        assertThat(messageQueue.isEmpty(), is(true));
        verifyZeroInteractions(mockSession);
    }

    @Test
    public void exitDoesNothing() {

        // given
        client.getMessageBuffer().append(new byte[] { (byte)0x01, (byte)0x02 });
        ClientConnectedState target = new ClientConnectedState(client);

        // when
        target.exit();

        // then
        assertThat(client.getMessageBuffer().length(), is(2));
        assertThat(messageQueue.isEmpty(), is(true));
        verifyZeroInteractions(mockSession);
    }

    @Test
    public void handleOutboundMessageClosesClientIfAppropriateMessageIsSent() {

        // given
        ClientConnectedState target = new ClientConnectedState(client);

        // when
        target.handleOutboundMessage(new DisconnectClient(false));

        // then
        assertThat(messageQueue.size(), is(1));
        assertThat(messageQueue.get(0), is(instanceOf(ClientDisconnected.class)));
        assertThat(client.getCurrentState(), is(instanceOf(ClientDisconnectedState.class)));
        verify(mockSession, times(1)).close();
        verifyNoMoreInteractions(mockSession);
    }

    @Test
    public void handleOutboundMessageDisconnectsClientIfAppropriateMessageIsSent() {

        // given
        ClientConnectedState target = new ClientConnectedState(client);

        // when
        target.handleOutboundMessage(new DisconnectClient(true));

        // then
        assertThat(messageQueue.size(), is(1));
        assertThat(messageQueue.get(0), is(instanceOf(ClientDisconnected.class)));
        assertThat(client.getCurrentState(), is(instanceOf(ClientDisconnectedState.class)));
        verify(mockSession, times(1)).terminate();
        verifyNoMoreInteractions(mockSession);
    }

    @Test
    public void handleOutboundMessageTerminatesClientIfMessageSerializationFails() {

        // given
        ClientConnectedState target = new ClientConnectedState(client);

        // when
        target.handleOutboundMessage(new NonSerializableMessage());

        // then
        assertThat(messageQueue.size(), is(1));
        assertThat(messageQueue.get(0), is(instanceOf(ClientDisconnected.class)));
        assertThat(client.getCurrentState(), is(instanceOf(ClientDisconnectedState.class)));
        verify(mockSession, times(1)).terminate();
        verifyNoMoreInteractions(mockSession);
    }

    @Test
    public void handleOutboundMessageTerminatesClientIfErrorOccursDuringSend() {

        // given
        ClientConnectedState target = new ClientConnectedState(client);
        doThrow(IOException.class).when(mockSession).write(ArgumentMatchers.any(byte[].class));

        // when
        target.handleOutboundMessage(new SerializableMessage());

        // then
        assertThat(messageQueue.size(), is(1));
        assertThat(messageQueue.get(0), is(instanceOf(ClientDisconnected.class)));
        assertThat(client.getCurrentState(), is(instanceOf(ClientDisconnectedState.class)));
        verify(mockSession, times(1)).write(ArgumentMatchers.any(byte[].class));
        verify(mockSession, times(1)).terminate();
        verifyNoMoreInteractions(mockSession);
    }

    @Test
    public void handleOutboundMessageWritesSerializedMessageToSession() throws IOException, ProtocolException {

        // given
        final List<byte[]> observedInWrite = new ArrayList<>();
        ClientConnectedState target = new ClientConnectedState(client);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                observedInWrite.add((byte[]) invocation.getArgument(0));
                return null;
            }
        }).when(mockSession).write(ArgumentMatchers.any(byte[].class));

        // when
        target.handleOutboundMessage(new SerializableMessage());

        // then
        assertThat(messageQueue.size(), is(0));
        assertThat(client.getCurrentState(), is(sameInstance(mockState)));
        verify(mockSession, times(1)).write(ArgumentMatchers.any(byte[].class));
        verifyNoMoreInteractions(mockSession);

        assertThat(observedInWrite.size(), is(1));
        MessageBuffer buffer = new MessageBuffer();
        buffer.append(observedInWrite.get(0));
        Message observedMessage = new MessageDeserializer(buffer).deserialize();
        assertThat(observedMessage, is(instanceOf(SerializableMessage.class)));
    }

    @Test
    public void handleDataReceivedDoesNothingIfThereIsNotEnoughToDeserialize() {

        // given
        ClientConnectedState target = new ClientConnectedState(client);
        client.getMessageBuffer().append(new byte[]{(byte)0x00, (byte)0x050});

        // when
        target.handleDataReceived();

        // then
        assertThat(client.getMessageBuffer().length(), is(2));
        assertThat(client.getCurrentState(), is(sameInstance(mockState)));
        verifyZeroInteractions(mockSession);
    }

    @Test
    public void handleDataReceivedTerminatesClientIfDeserializationFails() throws IOException, ProtocolException {

        // given
        byte[] data = new MessageSerializer(new SerializableMessage()).serialize();
        for (int i = 3; i < data.length; i++) {
            data[i] = (byte)0x7F; // overwrite message type identifier & payload
        }
        client.getMessageBuffer().append(data);

        ClientConnectedState target = new ClientConnectedState(client);

        // when
        target.handleDataReceived();

        // then
        assertThat(client.getCurrentState(), is(instanceOf(ClientDisconnectedState.class)));
        assertThat(messageQueue.size(), is(1));
        assertThat(messageQueue.get(0), is(instanceOf(ClientDisconnected.class)));
        verify(mockSession, times(1)).terminate();
        verifyNoMoreInteractions(mockSession);
    }

    @Test
    public void handleDataReceivedAddsDeserializedMessageToMessageQueue() throws IOException, ProtocolException {

        // given
        byte[] data = new MessageSerializer(new SerializableMessage()).serialize();
        client.getMessageBuffer().append(data);

        ClientConnectedState target = new ClientConnectedState(client);

        // when
        target.handleDataReceived();

        // then
        assertThat(client.getCurrentState(), is(sameInstance(mockState)));
        assertThat(client.getMessageBuffer().length(), is(0));
        assertThat(messageQueue.size(), is(1));
        assertThat(messageQueue.get(0), is(instanceOf(SerializableMessage.class)));
        verifyZeroInteractions(mockSession);

        // and when appending two more messages
        client.getMessageBuffer().append(data);
        client.getMessageBuffer().append(data);
        target.handleDataReceived();

        // then
        assertThat(client.getCurrentState(), is(sameInstance(mockState)));
        assertThat(client.getMessageBuffer().length(), is(0));
        assertThat(messageQueue.size(), is(3));
        assertThat(messageQueue.get(0), is(instanceOf(SerializableMessage.class)));
        assertThat(messageQueue.get(1), is(instanceOf(SerializableMessage.class)));
        assertThat(messageQueue.get(2), is(instanceOf(SerializableMessage.class)));
        verifyZeroInteractions(mockSession);
    }

    @Test
    public void handleSessionClosedMakesStateTransition() {

        // given
        ClientConnectedState target = new ClientConnectedState(client);

        // when
        target.handleSessionClosed(mockSession);

        // then
        assertThat(client.getCurrentState(), is(instanceOf(ClientDisconnectedState.class)));
        assertThat(messageQueue.size(), is(1));
        assertThat(messageQueue.get(0), is(instanceOf(ClientDisconnected.class)));
        verifyZeroInteractions(mockSession);
    }

    private static final class NonSerializableMessage implements Message {

        @Override
        public Class<? extends Message> getType() {
            return getClass();
        }
    }

    private static final class SerializableMessage implements Message, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public Class<? extends Message> getType() {
            return getClass();
        }
    }
}
