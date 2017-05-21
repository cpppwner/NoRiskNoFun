package gmbh.norisknofun.network;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import gmbh.norisknofun.GdxTest;
import gmbh.norisknofun.network.socket.SelectionResult;
import gmbh.norisknofun.network.socket.SocketFactory;
import gmbh.norisknofun.network.socket.SocketSelector;
import gmbh.norisknofun.network.socket.TCPClientSocket;
import gmbh.norisknofun.network.socket.TCPServerSocket;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


/**
 * Unit tests for Network Server.
 */
public class NetworkServerTests extends GdxTest {

    private static final int PORT = 27001;

    private NetworkServer server;

    private SocketSelector selectorMock;
    private TCPServerSocket serverSocketMock;
    private TCPClientSocket clientSocketMockOne;
    private TCPClientSocket clientSocketMockTwo;
    private SocketFactory socketFactoryMock;
    private SelectionResult selectionResultMock;
    private SessionEventHandler sessionEventHandlerMock;

    @Rule
    public Timeout globalTimeout= new Timeout(1, TimeUnit.MINUTES); // each test gets a timeout of 1 min


    @Before
    public void setUp() {

        selectorMock = mock(SocketSelector.class);
        serverSocketMock = mock(TCPServerSocket.class);
        clientSocketMockOne = mock(TCPClientSocket.class);
        clientSocketMockTwo = mock(TCPClientSocket.class);
        socketFactoryMock = mock(SocketFactory.class);
        selectionResultMock = mock(SelectionResult.class);
        sessionEventHandlerMock = mock(SessionEventHandler.class);

        server = new NetworkServer(socketFactoryMock, sessionEventHandlerMock);
    }

    @After
    public void tearDown() throws InterruptedException {

        server.stop();
    }

    @Test
    public void aDefaultConstructedNetworkServerIsNotRunning() {

        // when
        boolean obtained = server.isRunning();

        // then
        assertThat(obtained, is(false));
    }

    @Test
    public void startWhenOpeningSelectorThrowsException() throws IOException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenThrow(IOException.class);

        // when
        boolean obtained = server.start(PORT);

        // then
        assertThat(obtained, is(false));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(0)).openServerSocket(anyInt());
        verify(selectorMock, times(0)).register(any(TCPServerSocket.class));
        verify(selectorMock, times(0)).close();
        verify(serverSocketMock, times(0)).close();
    }

    @Test
    public void startWhenOpeningServerSocketThrowsException() throws IOException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openServerSocket(anyInt())).thenThrow(IOException.class);

        // when
        boolean obtained = server.start(PORT);

        // then
        assertThat(obtained, is(false));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openServerSocket(anyInt());
        verify(selectorMock, times(0)).register(any(TCPServerSocket.class));
        verify(selectorMock, times(1)).close();
        verify(serverSocketMock, times(0)).close();
    }

    @Test
    public void startWhenRegisteringServerSocketThrowsException() throws IOException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openServerSocket(anyInt())).thenReturn(serverSocketMock);
        doThrow(IOException.class).when(selectorMock).register(any(TCPServerSocket.class));

        // when
        boolean obtained = server.start(PORT);

        // then
        assertThat(obtained, is(false));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openServerSocket(anyInt());
        verify(selectorMock, times(1)).register(any(TCPServerSocket.class));
        verify(selectorMock, times(1)).close();
        verify(serverSocketMock, times(1)).close();
    }

    @Test
    public void closingSocketAndSelectorWhenExceptionIsThrownInClose() throws IOException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openServerSocket(anyInt())).thenReturn(serverSocketMock);
        doThrow(IOException.class).when(selectorMock).register(any(TCPServerSocket.class));
        doThrow(IOException.class).when(selectorMock).close();
        doThrow(IOException.class).when(serverSocketMock).close();

        // when
        boolean obtained = server.start(PORT);

        // then
        assertThat(obtained, is(false));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openServerSocket(anyInt());
        verify(selectorMock, times(1)).register(any(TCPServerSocket.class));
        verify(selectorMock, times(1)).close();
        verify(serverSocketMock, times(1)).close();
    }

    @Test
    public void whenSelectThrowsException() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openServerSocket(anyInt())).thenReturn(serverSocketMock);
        doThrow(IOException.class).when(selectorMock).select();

        final CountDownLatch selectorCloseLatch = new CountDownLatch(1);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                selectorCloseLatch.countDown();
                return null;
            }
        }).when(selectorMock).close();

        // when
        boolean obtained = server.start(PORT);
        selectorCloseLatch.await();
        server.stop();

        // then
        assertThat(obtained, is(true));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openServerSocket(anyInt());
        verify(selectorMock, times(1)).register(serverSocketMock);
        verify(selectorMock, times(1)).select();
        verify(selectorMock, times(1)).close();
        verify(serverSocketMock, times(1)).close();
    }

    @Test
    public void whenSelectReturnsEmptySets() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openServerSocket(anyInt())).thenReturn(serverSocketMock);
        doReturn(selectionResultMock).doThrow(IOException.class).when(selectorMock).select();
        doReturn(Collections.emptySet()).when(selectionResultMock).getAcceptableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getReadableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getWritableSockets();

        final CountDownLatch selectorCloseLatch = new CountDownLatch(1);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                selectorCloseLatch.countDown();
                return null;
            }
        }).when(selectorMock).close();

        // when
        boolean obtained = server.start(PORT);
        selectorCloseLatch.await();
        server.stop();

        // then
        assertThat(obtained, is(true));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openServerSocket(anyInt());
        verify(selectorMock, times(1)).register(serverSocketMock);
        verify(selectorMock, times(2)).select();
        verify(selectorMock, times(1)).close();
        verify(serverSocketMock, times(0)).accept();
        verify(serverSocketMock, times(1)).close();
    }

    @Test
    public void whenServerSocketIsInAcceptingStateButAcceptReturnsNull() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openServerSocket(anyInt())).thenReturn(serverSocketMock);
        doReturn(selectionResultMock).doThrow(IOException.class).when(selectorMock).select();
        doReturn(Collections.singleton(serverSocketMock)).when(selectionResultMock).getAcceptableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getReadableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getWritableSockets();
        doReturn(null).when(serverSocketMock).accept();

        final CountDownLatch selectorCloseLatch = new CountDownLatch(1);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                selectorCloseLatch.countDown();
                return null;
            }
        }).when(selectorMock).close();

        // when
        boolean obtained = server.start(PORT);
        selectorCloseLatch.await();
        server.stop();

        // then
        assertThat(obtained, is(true));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openServerSocket(anyInt());
        verify(selectorMock, times(1)).register(serverSocketMock);
        verify(selectorMock, times(2)).select();
        verify(selectorMock, times(1)).close();
        verify(serverSocketMock, times(1)).accept();
        verify(serverSocketMock, times(1)).close();
        verify(sessionEventHandlerMock, times(0)).newSession(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataReceived(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataWritten(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionClosed(any(Session.class));
    }

    @Test
    public void whenServerSocketIsInAcceptingStateButAcceptThrowsException() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openServerSocket(anyInt())).thenReturn(serverSocketMock);
        doReturn(selectionResultMock).doThrow(IOException.class).when(selectorMock).select();
        doReturn(Collections.singleton(serverSocketMock)).when(selectionResultMock).getAcceptableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getReadableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getWritableSockets();
        doThrow(IOException.class).when(serverSocketMock).accept();

        final CountDownLatch selectorCloseLatch = new CountDownLatch(1);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                selectorCloseLatch.countDown();
                return null;
            }
        }).when(selectorMock).close();

        // when
        boolean obtained = server.start(PORT);
        selectorCloseLatch.await();
        server.stop();

        // then
        assertThat(obtained, is(true));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openServerSocket(anyInt());
        verify(selectorMock, times(1)).register(serverSocketMock);
        verify(selectorMock, times(2)).select();
        verify(selectorMock, times(1)).close();
        verify(serverSocketMock, times(1)).accept();
        verify(serverSocketMock, times(1)).close();
        verify(sessionEventHandlerMock, times(0)).newSession(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataReceived(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataWritten(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionClosed(any(Session.class));
    }

    @Test
    public void whenServerSocketIsInAcceptingStateAndRegisteringFails() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openServerSocket(anyInt())).thenReturn(serverSocketMock);
        doReturn(selectionResultMock).doThrow(IOException.class).when(selectorMock).select();
        doReturn(Collections.singleton(serverSocketMock)).when(selectionResultMock).getAcceptableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getReadableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getWritableSockets();
        doReturn(clientSocketMockOne).doReturn(null).when(serverSocketMock).accept();
        doThrow(IOException.class).when(selectorMock).register(any(TCPClientSocket.class), anyBoolean());

        final CountDownLatch selectorCloseLatch = new CountDownLatch(1);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                selectorCloseLatch.countDown();
                return null;
            }
        }).when(selectorMock).close();

        // when
        boolean obtained = server.start(PORT);
        selectorCloseLatch.await();
        server.stop();

        // then
        assertThat(obtained, is(true));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openServerSocket(anyInt());
        verify(selectorMock, times(1)).register(serverSocketMock);
        verify(selectorMock, times(2)).select();
        verify(selectorMock, times(1)).close();
        verify(serverSocketMock, times(2)).accept();
        verify(serverSocketMock, times(1)).close();
        verify(clientSocketMockOne, times(1)).close();
        verify(sessionEventHandlerMock, times(0)).newSession(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataReceived(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataWritten(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionClosed(any(Session.class));
    }

    @Test
    public void whenServerSocketIsInAcceptingStateAndRegisteringAndClosingClientSocketFails() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openServerSocket(anyInt())).thenReturn(serverSocketMock);
        doReturn(selectionResultMock).doThrow(IOException.class).when(selectorMock).select();
        doReturn(Collections.singleton(serverSocketMock)).when(selectionResultMock).getAcceptableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getReadableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getWritableSockets();
        doReturn(clientSocketMockOne).doReturn(null).when(serverSocketMock).accept();
        doThrow(IOException.class).when(selectorMock).register(any(TCPClientSocket.class), anyBoolean());
        doThrow(IOException.class).when(clientSocketMockOne).close();

        final CountDownLatch selectorCloseLatch = new CountDownLatch(1);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                selectorCloseLatch.countDown();
                return null;
            }
        }).when(selectorMock).close();

        // when
        boolean obtained = server.start(PORT);
        selectorCloseLatch.await();
        server.stop();

        // then
        assertThat(obtained, is(true));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openServerSocket(anyInt());
        verify(selectorMock, times(1)).register(serverSocketMock);
        verify(selectorMock, times(2)).select();
        verify(selectorMock, times(1)).close();
        verify(serverSocketMock, times(2)).accept();
        verify(serverSocketMock, times(1)).close();
        verify(clientSocketMockOne, times(1)).close();
        verify(sessionEventHandlerMock, times(0)).newSession(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataReceived(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataWritten(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionClosed(any(Session.class));
    }

    @Test
    public void serverSocketIsInAcceptingStateAndOneConnectionIsAccepted() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openServerSocket(anyInt())).thenReturn(serverSocketMock);
        doReturn(selectionResultMock).doThrow(IOException.class).when(selectorMock).select();
        doReturn(Collections.singleton(serverSocketMock)).when(selectionResultMock).getAcceptableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getReadableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getWritableSockets();
        doReturn(clientSocketMockOne).doReturn(null).when(serverSocketMock).accept();

        final CountDownLatch selectorCloseLatch = new CountDownLatch(1);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                selectorCloseLatch.countDown();
                return null;
            }
        }).when(selectorMock).close();

        // when
        boolean obtained = server.start(PORT);
        selectorCloseLatch.await();
        server.stop();

        // then
        assertThat(obtained, is(true));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openServerSocket(anyInt());
        verify(selectorMock, times(1)).register(serverSocketMock);
        verify(selectorMock, times(2)).select();
        verify(selectorMock, times(1)).close();
        verify(serverSocketMock, times(2)).accept();
        verify(serverSocketMock, times(1)).close();
        verify(clientSocketMockOne, times(1)).close();
        verify(sessionEventHandlerMock, times(1)).newSession(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataReceived(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataWritten(any(Session.class));
        verify(sessionEventHandlerMock, times(1)).sessionClosed(any(Session.class));
    }

    @Test
    public void serverSocketIsInAcceptingStateAndMultipleConnectionsAreAccepted() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openServerSocket(anyInt())).thenReturn(serverSocketMock);
        doReturn(selectionResultMock).doThrow(IOException.class).when(selectorMock).select();
        doReturn(Collections.singleton(serverSocketMock)).when(selectionResultMock).getAcceptableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getReadableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getWritableSockets();
        doReturn(clientSocketMockOne).doReturn(clientSocketMockTwo).doReturn(null).when(serverSocketMock).accept();

        final CountDownLatch selectorCloseLatch = new CountDownLatch(1);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                selectorCloseLatch.countDown();
                return null;
            }
        }).when(selectorMock).close();

        // when
        boolean obtained = server.start(PORT);
        selectorCloseLatch.await();
        server.stop();

        // then
        assertThat(obtained, is(true));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openServerSocket(anyInt());
        verify(selectorMock, times(1)).register(serverSocketMock);
        verify(selectorMock, times(2)).select();
        verify(selectorMock, times(1)).close();
        verify(serverSocketMock, times(3)).accept();
        verify(serverSocketMock, times(1)).close();
        verify(clientSocketMockOne, times(1)).close();
        verify(clientSocketMockTwo, times(1)).close();
        verify(sessionEventHandlerMock, times(2)).newSession(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataReceived(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataWritten(any(Session.class));
        verify(sessionEventHandlerMock, times(2)).sessionClosed(any(Session.class));
    }

    @Test
    public void closingPreviouslyAcceptedClientConnectionsThrowsException() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openServerSocket(anyInt())).thenReturn(serverSocketMock);
        doReturn(selectionResultMock).doThrow(IOException.class).when(selectorMock).select();
        doReturn(Collections.singleton(serverSocketMock)).when(selectionResultMock).getAcceptableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getReadableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getWritableSockets();
        doReturn(clientSocketMockOne).doReturn(clientSocketMockTwo).doReturn(null).when(serverSocketMock).accept();
        doThrow(IOException.class).when(clientSocketMockOne).close();
        doThrow(IOException.class).when(clientSocketMockTwo).close();

        final CountDownLatch selectorCloseLatch = new CountDownLatch(1);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                selectorCloseLatch.countDown();
                return null;
            }
        }).when(selectorMock).close();

        // when
        boolean obtained = server.start(PORT);
        selectorCloseLatch.await();
        server.stop();

        // then
        assertThat(obtained, is(true));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openServerSocket(anyInt());
        verify(selectorMock, times(1)).register(serverSocketMock);
        verify(selectorMock, times(2)).select();
        verify(selectorMock, times(1)).close();
        verify(serverSocketMock, times(3)).accept();
        verify(serverSocketMock, times(1)).close();
        verify(clientSocketMockOne, times(1)).close();
        verify(clientSocketMockTwo, times(1)).close();
        verify(sessionEventHandlerMock, times(2)).newSession(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataReceived(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataWritten(any(Session.class));
        verify(sessionEventHandlerMock, times(2)).sessionClosed(any(Session.class));
    }

    @Test
    public void newlyCreatedSessionsAreClosedImmediatelyAfterAccept() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openServerSocket(anyInt())).thenReturn(serverSocketMock);
        doReturn(selectionResultMock).doThrow(IOException.class).when(selectorMock).select();
        doReturn(Collections.singleton(serverSocketMock)).when(selectionResultMock).getAcceptableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getReadableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getWritableSockets();
        doReturn(clientSocketMockOne).doReturn(clientSocketMockTwo).doReturn(null).when(serverSocketMock).accept();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Session session = invocation.getArgument(0);
                session.close();

                return null;
            }
        }).when(sessionEventHandlerMock).newSession(any(Session.class));

        final CountDownLatch selectorCloseLatch = new CountDownLatch(1);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                selectorCloseLatch.countDown();
                return null;
            }
        }).when(selectorMock).close();

        // when
        boolean obtained = server.start(PORT);
        selectorCloseLatch.await();
        server.stop();

        // then
        assertThat(obtained, is(true));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openServerSocket(anyInt());
        verify(selectorMock, times(1)).register(serverSocketMock);
        verify(selectorMock, times(2)).select();
        verify(selectorMock, times(1)).close();
        verify(serverSocketMock, times(3)).accept();
        verify(serverSocketMock, times(1)).close();
        verify(clientSocketMockOne, times(1)).close();
        verify(clientSocketMockTwo, times(1)).close();
        verify(sessionEventHandlerMock, times(2)).newSession(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataReceived(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataWritten(any(Session.class));
        verify(sessionEventHandlerMock, times(2)).sessionClosed(any(Session.class));
    }

    @Test
    public void newlyCreatedSessionsAreClosedImmediatelyAfterAcceptAndClosingSocketThrowsException() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openServerSocket(anyInt())).thenReturn(serverSocketMock);
        doReturn(selectionResultMock).doThrow(IOException.class).when(selectorMock).select();
        doReturn(Collections.singleton(serverSocketMock)).when(selectionResultMock).getAcceptableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getReadableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getWritableSockets();
        doReturn(clientSocketMockOne).doReturn(clientSocketMockTwo).doReturn(null).when(serverSocketMock).accept();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Session session = invocation.getArgument(0);
                session.close();

                return null;
            }
        }).when(sessionEventHandlerMock).newSession(any(Session.class));
        doThrow(IOException.class).when(clientSocketMockOne).close();
        doThrow(IOException.class).when(clientSocketMockTwo).close();

        final CountDownLatch selectorCloseLatch = new CountDownLatch(1);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                selectorCloseLatch.countDown();
                return null;
            }
        }).when(selectorMock).close();

        // when
        boolean obtained = server.start(PORT);
        selectorCloseLatch.await();
        server.stop();

        // then
        assertThat(obtained, is(true));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openServerSocket(anyInt());
        verify(selectorMock, times(1)).register(serverSocketMock);
        verify(selectorMock, times(2)).select();
        verify(selectorMock, times(1)).close();
        verify(serverSocketMock, times(3)).accept();
        verify(serverSocketMock, times(1)).close();
        verify(clientSocketMockOne, times(1)).close();
        verify(clientSocketMockTwo, times(1)).close();
        verify(sessionEventHandlerMock, times(2)).newSession(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataReceived(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataWritten(any(Session.class));
        verify(sessionEventHandlerMock, times(2)).sessionClosed(any(Session.class));
    }

    @Test
    public void whenModifyingClientConnectionFails() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openServerSocket(anyInt())).thenReturn(serverSocketMock);
        doReturn(selectionResultMock).doThrow(IOException.class).when(selectorMock).select();
        doReturn(Collections.singleton(serverSocketMock)).when(selectionResultMock).getAcceptableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getReadableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getWritableSockets();
        doReturn(clientSocketMockOne).doReturn(clientSocketMockTwo).doReturn(null).when(serverSocketMock).accept();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Session session = invocation.getArgument(0);
                session.write("Hello World!".getBytes());

                return null;
            }
        }).when(sessionEventHandlerMock).newSession(any(Session.class));
        doThrow(IOException.class).when(selectorMock).modify(any(TCPClientSocket.class), anyBoolean());

        final CountDownLatch selectorCloseLatch = new CountDownLatch(1);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                selectorCloseLatch.countDown();
                return null;
            }
        }).when(selectorMock).close();

        // when
        boolean obtained = server.start(PORT);
        selectorCloseLatch.await();
        server.stop();

        // then
        assertThat(obtained, is(true));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openServerSocket(anyInt());
        verify(selectorMock, times(1)).register(serverSocketMock);
        verify(selectorMock, times(2)).select();
        verify(selectorMock, times(1)).close();
        verify(serverSocketMock, times(3)).accept();
        verify(serverSocketMock, times(1)).close();
        verify(clientSocketMockOne, times(1)).close();
        verify(clientSocketMockTwo, times(1)).close();
        verify(sessionEventHandlerMock, times(2)).newSession(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataReceived(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataWritten(any(Session.class));
        verify(sessionEventHandlerMock, times(2)).sessionClosed(any(Session.class));
        verify(selectorMock, times(1)).modify(clientSocketMockOne, true);
        verify(selectorMock, times(1)).modify(clientSocketMockTwo, true);
    }

    @Test
    public void clientSocketHasDataToWrite() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openServerSocket(anyInt())).thenReturn(serverSocketMock);
        doReturn(selectionResultMock).doReturn(selectionResultMock).doThrow(IOException.class).when(selectorMock).select();
        doReturn(Collections.singleton(serverSocketMock)).doReturn(Collections.emptySet()).when(selectionResultMock).getAcceptableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getReadableSockets();
        doReturn(Collections.emptySet())
                .doReturn(new HashSet<>(Arrays.asList(clientSocketMockOne, clientSocketMockTwo)))
                .when(selectionResultMock).getWritableSockets();
        doReturn(clientSocketMockOne).doReturn(clientSocketMockTwo).doReturn(null).when(serverSocketMock).accept();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Session session = invocation.getArgument(0);
                session.write("Hello World!".getBytes());

                return null;
            }
        }).when(sessionEventHandlerMock).newSession(any(Session.class));
        Answer writeAnswer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ByteBuffer data = invocation.getArgument(0);
                byte[] consumed = new byte[data.remaining()];
                data.get(consumed);

                return consumed.length;
            }
        };

        doAnswer(writeAnswer).when(clientSocketMockOne).write(any(ByteBuffer.class));
        doAnswer(writeAnswer).when(clientSocketMockTwo).write(any(ByteBuffer.class));

        final CountDownLatch selectorCloseLatch = new CountDownLatch(1);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                selectorCloseLatch.countDown();
                return null;
            }
        }).when(selectorMock).close();

        // when
        boolean obtained = server.start(PORT);
        selectorCloseLatch.await();
        server.stop();

        // then
        assertThat(obtained, is(true));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openServerSocket(anyInt());
        verify(selectorMock, times(1)).register(serverSocketMock);
        verify(selectorMock, times(3)).select();
        verify(selectorMock, times(1)).close();
        verify(serverSocketMock, times(3)).accept();
        verify(serverSocketMock, times(1)).close();
        verify(clientSocketMockOne, times(1)).close();
        verify(clientSocketMockTwo, times(1)).close();
        verify(clientSocketMockOne, times(1)).write(any(ByteBuffer.class));
        verify(clientSocketMockTwo, times(1)).write(any(ByteBuffer.class));
        verify(sessionEventHandlerMock, times(2)).newSession(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataReceived(any(Session.class));
        verify(sessionEventHandlerMock, times(2)).sessionDataWritten(any(Session.class));
        verify(sessionEventHandlerMock, times(2)).sessionClosed(any(Session.class));
        verify(selectorMock, times(1)).modify(clientSocketMockOne, true);
        verify(selectorMock, times(1)).modify(clientSocketMockTwo, true);
        verify(selectorMock, times(1)).modify(clientSocketMockOne, false);
        verify(selectorMock, times(1)).modify(clientSocketMockTwo, false);
    }

    @Test
    public void clientSocketHasDataToWriteButWritingToSocketThrowsException() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openServerSocket(anyInt())).thenReturn(serverSocketMock);
        doReturn(selectionResultMock).doReturn(selectionResultMock).doThrow(IOException.class).when(selectorMock).select();
        doReturn(Collections.singleton(serverSocketMock)).doReturn(Collections.emptySet()).when(selectionResultMock).getAcceptableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getReadableSockets();
        doReturn(Collections.emptySet())
                .doReturn(new HashSet<>(Arrays.asList(clientSocketMockOne, clientSocketMockTwo)))
                .when(selectionResultMock).getWritableSockets();
        doReturn(clientSocketMockOne).doReturn(clientSocketMockTwo).doReturn(null).when(serverSocketMock).accept();
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Session session = invocation.getArgument(0);
                session.write("Hello World!".getBytes());

                return null;
            }
        }).when(sessionEventHandlerMock).newSession(any(Session.class));

        Answer writeAnswer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                ByteBuffer data = invocation.getArgument(0);
                byte[] consumed = new byte[data.remaining()];
                data.get(consumed);

                return consumed.length;
            }
        };
        doAnswer(writeAnswer).when(clientSocketMockOne).write(any(ByteBuffer.class));
        doThrow(IOException.class).when(clientSocketMockTwo).write(any(ByteBuffer.class));

        final CountDownLatch selectorCloseLatch = new CountDownLatch(1);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                selectorCloseLatch.countDown();
                return null;
            }
        }).when(selectorMock).close();

        // when
        boolean obtained = server.start(PORT);
        selectorCloseLatch.await();
        server.stop();

        // then
        assertThat(obtained, is(true));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openServerSocket(anyInt());
        verify(selectorMock, times(1)).register(serverSocketMock);
        verify(selectorMock, times(3)).select();
        verify(selectorMock, times(1)).close();
        verify(serverSocketMock, times(3)).accept();
        verify(serverSocketMock, times(1)).close();
        verify(clientSocketMockOne, times(1)).close();
        verify(clientSocketMockTwo, times(1)).close();
        verify(clientSocketMockOne, times(1)).write(any(ByteBuffer.class));
        verify(clientSocketMockTwo, times(1)).write(any(ByteBuffer.class));
        verify(sessionEventHandlerMock, times(2)).newSession(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataReceived(any(Session.class));
        verify(sessionEventHandlerMock, times(1)).sessionDataWritten(any(Session.class));
        verify(sessionEventHandlerMock, times(2)).sessionClosed(any(Session.class));
        verify(selectorMock, times(1)).modify(clientSocketMockOne, true);
        verify(selectorMock, times(1)).modify(clientSocketMockTwo, true);
        verify(selectorMock, times(1)).modify(clientSocketMockOne, false);
        verify(selectorMock, times(0)).modify(clientSocketMockTwo, false);
    }

    @Test
    public void clientSocketHasDataToRead() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openServerSocket(anyInt())).thenReturn(serverSocketMock);
        doReturn(selectionResultMock).doReturn(selectionResultMock).doThrow(IOException.class).when(selectorMock).select();
        doReturn(Collections.singleton(serverSocketMock)).doReturn(Collections.emptySet()).when(selectionResultMock).getAcceptableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getWritableSockets();
        doReturn(Collections.emptySet())
                .doReturn(new HashSet<>(Arrays.asList(clientSocketMockOne, clientSocketMockTwo)))
                .when(selectionResultMock).getReadableSockets();
        doReturn(clientSocketMockOne).doReturn(clientSocketMockTwo).doReturn(null).when(serverSocketMock).accept();

        Answer readAnswer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                byte[] bytes = "Hello World!".getBytes();
                ByteBuffer data = invocation.getArgument(0);
                data.put(bytes);

                return bytes.length;
            }
        };

        doAnswer(readAnswer).when(clientSocketMockOne).read(any(ByteBuffer.class));
        doAnswer(readAnswer).when(clientSocketMockTwo).read(any(ByteBuffer.class));

        final CountDownLatch selectorCloseLatch = new CountDownLatch(1);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                selectorCloseLatch.countDown();
                return null;
            }
        }).when(selectorMock).close();

        // when
        boolean obtained = server.start(PORT);
        selectorCloseLatch.await();
        server.stop();

        // then
        assertThat(obtained, is(true));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openServerSocket(anyInt());
        verify(selectorMock, times(1)).register(serverSocketMock);
        verify(selectorMock, times(3)).select();
        verify(selectorMock, times(1)).close();
        verify(serverSocketMock, times(3)).accept();
        verify(serverSocketMock, times(1)).close();
        verify(clientSocketMockOne, times(1)).close();
        verify(clientSocketMockTwo, times(1)).close();
        verify(clientSocketMockOne, times(1)).read(any(ByteBuffer.class));
        verify(clientSocketMockTwo, times(1)).read(any(ByteBuffer.class));
        verify(sessionEventHandlerMock, times(2)).newSession(any(Session.class));
        verify(sessionEventHandlerMock, times(2)).sessionDataReceived(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataWritten(any(Session.class));
        verify(sessionEventHandlerMock, times(2)).sessionClosed(any(Session.class));
        verify(selectorMock, times(2)).modify(clientSocketMockOne, false);
        verify(selectorMock, times(2)).modify(clientSocketMockTwo, false);
    }

    @Test
    public void clientSocketHasDataToReadButReadingFromSocketThrowsException() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openServerSocket(anyInt())).thenReturn(serverSocketMock);
        doReturn(selectionResultMock).doReturn(selectionResultMock).doThrow(IOException.class).when(selectorMock).select();
        doReturn(Collections.singleton(serverSocketMock)).doReturn(Collections.emptySet()).when(selectionResultMock).getAcceptableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getWritableSockets();
        doReturn(Collections.emptySet())
                .doReturn(new HashSet<>(Arrays.asList(clientSocketMockOne, clientSocketMockTwo)))
                .when(selectionResultMock).getReadableSockets();
        doReturn(clientSocketMockOne).doReturn(clientSocketMockTwo).doReturn(null).when(serverSocketMock).accept();

        Answer readAnswer = new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                byte[] bytes = "Hello World!".getBytes();
                ByteBuffer data = invocation.getArgument(0);
                data.put(bytes);

                return bytes.length;
            }
        };

        doAnswer(readAnswer).when(clientSocketMockOne).read(any(ByteBuffer.class));
        doThrow(IOException.class).when(clientSocketMockTwo).read(any(ByteBuffer.class));

        final CountDownLatch selectorCloseLatch = new CountDownLatch(1);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                selectorCloseLatch.countDown();
                return null;
            }
        }).when(selectorMock).close();

        // when
        boolean obtained = server.start(PORT);
        selectorCloseLatch.await();
        server.stop();

        // then
        assertThat(obtained, is(true));
        assertThat(server.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openServerSocket(anyInt());
        verify(selectorMock, times(1)).register(serverSocketMock);
        verify(selectorMock, times(3)).select();
        verify(selectorMock, times(1)).close();
        verify(serverSocketMock, times(3)).accept();
        verify(serverSocketMock, times(1)).close();
        verify(clientSocketMockOne, times(1)).close();
        verify(clientSocketMockTwo, times(1)).close();
        verify(clientSocketMockOne, times(1)).read(any(ByteBuffer.class));
        verify(clientSocketMockTwo, times(1)).read(any(ByteBuffer.class));
        verify(sessionEventHandlerMock, times(2)).newSession(any(Session.class));
        verify(sessionEventHandlerMock, times(1)).sessionDataReceived(any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataWritten(any(Session.class));
        verify(sessionEventHandlerMock, times(2)).sessionClosed(any(Session.class));
        verify(selectorMock, times(2)).modify(clientSocketMockOne, false);
        verify(selectorMock, times(1)).modify(clientSocketMockTwo, false);
    }
}
