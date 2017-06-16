package gmbh.norisknofun.game.client;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import gmbh.norisknofun.game.gamemessages.client.ClientDisconnected;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.network.Session;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Tests for testing {@link ClientDisconnectedStateTests}.
 */
public class ClientDisconnectedStateTests {

    private LinkedList<Message> messageQueue;
    private Client client;
    private Session mockSession;
    private Message mockMessage;

    @Before
    public void setUp() {

        mockSession = mock(Session.class);
        mockMessage = mock(Message.class);
        ClientState mockState = mock(ClientState.class);

        messageQueue = new LinkedList<>();
        client = new Client(messageQueue);
        client.setSession(mockSession);
        client.setState(mockState);
    }

    @Test
    public void enterClearsMessageBuffer() {

        // given
        client.getMessageBuffer().append(new byte[] { (byte)0x01, (byte)0x02 });
        ClientDisconnectedState target = new ClientDisconnectedState(client);

        // when
        target.enter();

        // then
        assertThat(client.getMessageBuffer().length(), is(0));
        assertThat(messageQueue.size(), is(1));
        assertThat(messageQueue.get(0), is(instanceOf(ClientDisconnected.class)));
        verifyZeroInteractions(mockSession);
    }

    @Test
    public void exitDoesNothing() {

        // given
        client.getMessageBuffer().append(new byte[] { (byte)0x01, (byte)0x02 });
        ClientDisconnectedState target = new ClientDisconnectedState(client);

        // when
        target.exit();

        // then
        assertThat(client.getMessageBuffer().length(), is(2));
        assertThat(messageQueue.isEmpty(), is(true));
        verifyZeroInteractions(mockSession);
    }

    @Test
    public void handleOutboundMessageDoesNothing() {

        // given
        ClientDisconnectedState target = new ClientDisconnectedState(client);

        // when
        target.handleOutboundMessage(mockMessage);

        // then
        assertThat(messageQueue.isEmpty(), is(true));
        verifyZeroInteractions(mockSession, mockMessage);
    }

    @Test
    public void handleSessionClosedDoesNothing() {

        // given
        ClientDisconnectedState target = new ClientDisconnectedState(client);

        // when
        target.handleSessionClosed(mockSession);

        // then
        assertThat(messageQueue.isEmpty(), is(true));
        verifyZeroInteractions(mockSession);
    }

    @Test
    public void handleDataReceivedClearsMessageBuffer() {

        // given
        ClientDisconnectedState target = new ClientDisconnectedState(client);

        // when
        client.getMessageBuffer().append(new byte[] { (byte)0x01, (byte)0x02 });
        target.handleDataReceived();

        // then
        assertThat(client.getMessageBuffer().length(), is(0));
        assertThat(messageQueue.isEmpty(), is(true));
        verifyZeroInteractions(mockSession);
    }
}
