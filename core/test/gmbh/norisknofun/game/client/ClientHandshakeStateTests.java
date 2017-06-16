package gmbh.norisknofun.game.client;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import gmbh.norisknofun.GdxTest;
import gmbh.norisknofun.game.gamemessages.client.ClientConnectionRefused;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.protocol.MessageDeserializer;
import gmbh.norisknofun.game.protocol.ProtocolException;
import gmbh.norisknofun.game.protocol.messages.handshake.Handshake;
import gmbh.norisknofun.game.protocol.messages.handshake.HandshakeConstants;
import gmbh.norisknofun.game.protocol.util.MessageBuffer;
import gmbh.norisknofun.network.Session;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Test class for testing {@link ClientHandshakeState}.
 */
public class ClientHandshakeStateTests extends GdxTest {

    private LinkedList<Message> messageQueue;
    private Client client;
    private Session mockSession;
    private Message mockMessage;

    @Before
    public void setUp() {

        mockSession = mock(Session.class);
        mockMessage = mock(Message.class);

        messageQueue = new LinkedList<>();
        client = new Client(messageQueue);
        client.setSession(mockSession);

    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void enterStateSendsHandshakeMessageImmediately() throws IOException, ProtocolException {

        // given
        final List<byte[]> observedDataInWrite = new LinkedList<>();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                observedDataInWrite.add((byte[]) invocation.getArgument(0));
                return null;
            }
        }).when(mockSession).write(ArgumentMatchers.any(byte[].class));

        ClientHandshakeState target = new ClientHandshakeState(client);

        // when
        client.setState(target);

        // then
        assertThat(observedDataInWrite.size(), is(1));
        assertThat(messageQueue.size(), is(0));

        // check the sent message
        MessageBuffer buffer = new MessageBuffer();
        buffer.append(observedDataInWrite.get(0));
        Message obtainedMessage = new MessageDeserializer(buffer).deserialize();

        assertThat(obtainedMessage, is(instanceOf(Handshake.class)));
        assertThat(((Handshake)obtainedMessage).getProtocolMagic(), is(HandshakeConstants.HANDSHAKE_MAGIC));
        assertThat(((Handshake)obtainedMessage).getProtocolVersion(), is(HandshakeConstants.HANDSHAKE_PROTOCOL_VERSION));

        verify(mockSession, times(1)).write(ArgumentMatchers.any(byte[].class));
        verifyNoMoreInteractions(mockSession);
    }

    @Test
    public void whenSendingHandshakeMessageFailsClientIsTerminated() {

        // given
        doThrow(IOException.class).when(mockSession).write(ArgumentMatchers.any(byte[].class));

        ClientHandshakeState target = new ClientHandshakeState(client);

        // when
        client.setState(target);

        // then
        assertThat(client.getCurrentState(), is(instanceOf(ClientDisconnectedState.class)));
        assertThat(messageQueue.size(), is(1));
        assertThat(messageQueue.get(0), is(instanceOf(ClientConnectionRefused.class)));
        assertThat(((ClientConnectionRefused)messageQueue.get(0)).getReason(), Matchers.startsWith("Failed to send handshake: "));

        verify(mockSession, times(1)).write(ArgumentMatchers.any(byte[].class));
        verify(mockSession, times(1)).terminate();
        verifyNoMoreInteractions(mockSession);
    }

    @Test
    public void handleOutboundMessageThrowsException() {

        // given
        ClientHandshakeState target = new ClientHandshakeState(client);

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("no outbound message excepted here");

        // when/then
        target.handleOutboundMessage(mockMessage);
    }

    @Test
    public void handleNewSessionThrowsException() {

        // given
        ClientHandshakeState target = new ClientHandshakeState(client);

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("not expecting new session during handshake");

        // when/then
        target.handleNewSession(mockSession);
    }

    @Test
    public void handleSessionClosedMakesStateTransition() {

        // given
        doThrow(IOException.class).when(mockSession).write(ArgumentMatchers.any(byte[].class));

        ClientHandshakeState target = new ClientHandshakeState(client);

        // when
        target.handleSessionClosed(mockSession);

        // then
        assertThat(client.getCurrentState(), is(instanceOf(ClientDisconnectedState.class)));
        assertThat(messageQueue.size(), is(1));
        assertThat(messageQueue.get(0), is(instanceOf(ClientConnectionRefused.class)));
        assertThat(((ClientConnectionRefused)messageQueue.get(0)).getReason(), is("connection closed by server"));

        verifyZeroInteractions(mockSession);
    }

    @Test
    public void handleDataReceivedDoesNothingIfThereIsNotEnoughDataToDeserializeMessage() {

    }
}
