package gmbh.norisknofun.game.server.clients;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import gmbh.norisknofun.game.server.MessageBus;
import gmbh.norisknofun.network.Session;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * Tests for testing the {@link Client} container class.
 */
public class ClientsTests {

    private Session sessionOneMock;
    private Session sessionTwoMock;

    private ClientState clientStateOneMock;
    private ClientState clientStateTwoMock;

    private Client clientOne;
    private Client clientTwo;

    @Before
    public void setUp() {

        sessionOneMock = mock(Session.class);
        sessionTwoMock = mock(Session.class);

        clientStateOneMock = mock(ClientState.class);
        clientStateTwoMock = mock(ClientState.class);

        clientOne = new Client(sessionOneMock, mock(MessageBus.class));
        clientTwo = new Client(sessionTwoMock, mock(MessageBus.class));

        clientOne.setState(clientStateOneMock);
        clientTwo.setState(clientStateTwoMock);
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void aDefaultConstructedClientsContainerHasNoRegisteredClients() {

        // given
        Clients target = new Clients();

        // when
        int obtained = target.getNumberOfRegisteredClients();

        // then
        assertThat(obtained, is(0));
    }

    @Test
    public void registeringAClientWithNullSessionThrowsException() {

        // given
        Clients target = new Clients();

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("session is null");

        // when/then
        target.registerNewClient(null, clientOne);
    }

    @Test
    public void registeringAClientWithNullClientThrowsException() {

        // given
        Clients target = new Clients();

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("client is null");

        // when/then
        target.registerNewClient(sessionOneMock, null);
    }

    @Test
    public void registeringClientAddsNewClientToInternalContainer() {

        // given
        Clients target = new Clients();

        // when registering first client
        target.registerNewClient(sessionOneMock, clientOne);

        // then
        assertThat(target.getNumberOfRegisteredClients(), is(1));

        // and when registering the second client
        target.registerNewClient(sessionTwoMock, clientTwo);

        // then
        assertThat(target.getNumberOfRegisteredClients(), is(2));
    }

    @Test
    public void closeAllClosesAllSessionsNotifiesClientsAndClearsInternalContainer() {

        // given
        Clients target = new Clients();

        // when calling on empty container
        target.closeAll();

        // then
        assertThat(target.getNumberOfRegisteredClients(), is(0));

        // when registering some clients and calling closeAll
        target.registerNewClient(sessionOneMock, clientOne);
        target.registerNewClient(sessionTwoMock, clientTwo);
        target.closeAll();

        // then
        assertThat(target.getNumberOfRegisteredClients(), is(0));
        verify(sessionOneMock, times(1)).close();
        verify(sessionTwoMock, times(1)).close();
        verifyNoMoreInteractions(sessionOneMock, sessionTwoMock);
    }

    @Test
    public void processDataReceivedForwardsToClient() {

        // given
        Clients target = new Clients();
        target.registerNewClient(sessionOneMock, clientOne);
        target.registerNewClient(sessionTwoMock, clientTwo);

        doReturn(new byte[] {(byte)0x01, (byte)0x02}).when(sessionOneMock).read();
        doReturn(new byte[] {(byte)0x03, (byte)0x04}).when(sessionTwoMock).read();

        // when processing data for first session
        target.processDataReceived(sessionOneMock);

        // then
        assertThat(clientOne.getMessageBuffer().length(), is(2));
        verify(sessionOneMock, times(1)).read();
        verify(clientStateOneMock, times(1)).processDataReceived();
        verifyNoMoreInteractions(sessionOneMock);
        verifyZeroInteractions(sessionTwoMock);

        // and when processing data for second session
        target.processDataReceived(sessionTwoMock);

        assertThat(clientOne.getMessageBuffer().length(), is(2));
        assertThat(clientTwo.getMessageBuffer().length(), is(2));
        verify(sessionOneMock, times(1)).read();
        verify(clientStateOneMock, times(1)).processDataReceived();
        verify(sessionTwoMock, times(1)).read();
        verify(clientStateTwoMock, times(1)).processDataReceived();
        verifyNoMoreInteractions(sessionOneMock, sessionTwoMock);
    }

    @Test
    public void processDataReceivedDoesNothingIfSessionWasNotRegisteredBefore() {

        // given
        Clients target = new Clients();

        doReturn(new byte[] {(byte)0x01, (byte)0x02}).when(sessionOneMock).read();
        doReturn(new byte[] {(byte)0x03, (byte)0x04}).when(sessionTwoMock).read();

        // when processing data for first session
        target.processDataReceived(sessionOneMock);

        // then
        assertThat(clientOne.getMessageBuffer().length(), is(0));
        verify(clientStateOneMock, times(0)).processDataReceived();
        verifyZeroInteractions(sessionOneMock, sessionTwoMock);

        // and when processing data for second session
        target.processDataReceived(sessionTwoMock);

        assertThat(clientOne.getMessageBuffer().length(), is(0));
        assertThat(clientTwo.getMessageBuffer().length(), is(0));
        verify(clientStateOneMock, times(0)).processDataReceived();
        verify(clientStateTwoMock, times(0)).processDataReceived();
        verifyZeroInteractions(sessionOneMock, sessionTwoMock);
    }

    @Test
    public void processSessionClosedForwardsToClient() {

        // given
        Clients target = new Clients();
        target.registerNewClient(sessionOneMock, clientOne);
        target.registerNewClient(sessionTwoMock, clientTwo);

        // when processing data for first session
        target.processSessionClosed(sessionOneMock);

        // then
        verify(clientStateOneMock, times(1)).sessionClosed();
        verifyZeroInteractions(sessionOneMock, sessionTwoMock);

        // and when processing data for second session
        target.processSessionClosed(sessionTwoMock);

        verify(clientStateOneMock, times(1)).sessionClosed();
        verify(clientStateTwoMock, times(1)).sessionClosed();
        verifyZeroInteractions(sessionOneMock, sessionTwoMock);
    }

    @Test
    public void processSessionClosedDoesNothingIfSessionWasNotRegisteredBefore() {

        // given
        Clients target = new Clients();

        // when processing data for first session
        target.processSessionClosed(sessionOneMock);

        // then
        verify(clientStateOneMock, times(0)).sessionClosed();
        verifyZeroInteractions(sessionOneMock, sessionTwoMock);

        // and when processing data for second session
        target.processSessionClosed(sessionTwoMock);

        verify(clientStateOneMock, times(0)).sessionClosed();
        verify(clientStateTwoMock, times(0)).sessionClosed();
        verifyZeroInteractions(sessionOneMock, sessionTwoMock);
    }

}
