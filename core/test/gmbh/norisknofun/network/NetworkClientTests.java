package gmbh.norisknofun.network;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import gmbh.norisknofun.GdxTest;
import gmbh.norisknofun.network.socket.SelectionResult;
import gmbh.norisknofun.network.socket.SocketFactory;
import gmbh.norisknofun.network.socket.SocketSelector;
import gmbh.norisknofun.network.socket.TCPClientSocket;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for NetworkClient
 */
public class NetworkClientTests extends GdxTest {

    private static final String HOST = "localhost";
    private static final int PORT = 27001;

    private SocketSelector selectorMock;
    private TCPClientSocket socketMock;
    private SocketFactory socketFactoryMock;
    private SessionEventHandler sessionEventHandlerMock;
    private SelectionResult selectionResultMock;

    private NetworkClient client;

    @Rule
    public Timeout globalTimeout= new Timeout(1, TimeUnit.MINUTES); // each test gets a timeout of 1 min

    @Before
    public void setUp() {

        // setup mocks
        selectorMock = mock(SocketSelector.class);
        socketMock = mock(TCPClientSocket.class);
        socketFactoryMock = mock(SocketFactory.class);
        sessionEventHandlerMock = mock(SessionEventHandler.class);
        selectionResultMock = mock(SelectionResult.class);

        // create client instance
        client = new NetworkClient(socketFactoryMock, sessionEventHandlerMock);
    }

    @After
    public void tearDown() throws InterruptedException {

        client.stop();
    }

    @Test
    public void aDefaultConstructedNetworkClientIsNotRunning() {

        // when
        boolean obtained = client.isRunning();

        // then
        assertThat(obtained, is(false));
    }

    @Test
    public void connectWhenOpeningSelectorThrowsException() throws IOException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenThrow(IOException.class);

        // when
        boolean obtained = client.connect(HOST, PORT);

        // then
        assertThat(obtained, is(false));
        assertThat(client.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(0)).openClientSocket(anyString(), anyInt());
        verify(selectorMock, times(0)).register(any(TCPClientSocket.class), anyBoolean());
        verify(selectorMock, times(0)).close();
        verify(socketMock, times(0)).close();
    }

    @Test
    public void connectWhenOpeningClientSocketThrowsException() throws IOException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openClientSocket(anyString(), anyInt())).thenThrow(IOException.class);

        // when
        boolean obtained = client.connect(HOST, PORT);

        // then
        assertThat(obtained, is(false));
        assertThat(client.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openClientSocket(anyString(), anyInt());
        verify(selectorMock, times(0)).register(any(TCPClientSocket.class), anyBoolean());
        verify(selectorMock, times(1)).close();
        verify(socketMock, times(0)).close();
    }

    @Test
    public void connectWhenRegisteringClientSocketThrowsException() throws IOException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openClientSocket(anyString(), anyInt())).thenReturn(socketMock);
        doThrow(IOException.class).when(selectorMock).register(ArgumentMatchers.any(TCPClientSocket.class), anyBoolean());

        // when
        boolean obtained = client.connect(HOST, PORT);

        // then
        assertThat(obtained, is(false));
        assertThat(client.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openClientSocket(anyString(), anyInt());
        verify(selectorMock, times(1)).register(any(TCPClientSocket.class), anyBoolean());
        verify(selectorMock, times(1)).close();
        verify(socketMock, times(1)).close();
    }

    @Test
    public void closingSocketAndSelectorWhenExceptionIsThrownInClose() throws IOException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openClientSocket(anyString(), anyInt())).thenReturn(socketMock);
        doThrow(IOException.class).when(selectorMock).register(ArgumentMatchers.any(TCPClientSocket.class), anyBoolean());
        doThrow(IOException.class).when(selectorMock).close();
        doThrow(IOException.class).when(socketMock).close();

        // when
        boolean obtained = client.connect(HOST, PORT);

        // then
        assertThat(obtained, is(false));
        assertThat(client.isRunning(), is(false));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openClientSocket(anyString(), anyInt());
        verify(selectorMock, times(1)).register(any(TCPClientSocket.class), anyBoolean());
        verify(selectorMock, times(1)).close();
        verify(socketMock, times(1)).close();
    }

    @Test
    public void connectWhenAllMocksSucceed() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openClientSocket(anyString(), anyInt())).thenReturn(socketMock);

        final Session[] newSession = {null};
        final Session[] closedSession = {null};

        final CountDownLatch newSessionCountDownLatch = new CountDownLatch(1);
        final CountDownLatch newSessionAcceptedCountDownLatch = new CountDownLatch(1);
        final CountDownLatch sessionClosedCountdownLatch = new CountDownLatch(1);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                newSession[0] = invocation.getArgument(0);
                newSessionCountDownLatch.countDown();
                newSessionAcceptedCountDownLatch.await();
                newSession[0].close(); // close session immediately

                return null;
            }
        }).when(sessionEventHandlerMock).newSession(ArgumentMatchers.any(Session.class));

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                closedSession[0] = invocation.getArgument(0);
                sessionClosedCountdownLatch.countDown();

                return null;
            }
        }).when(sessionEventHandlerMock).sessionClosed(ArgumentMatchers.any(Session.class));

        // when
        boolean obtained = client.connect(HOST, PORT);
        newSessionCountDownLatch.await(); // client thread is blocked for now

        // then
        assertThat(obtained, is(true));
        assertThat(newSession[0], is(notNullValue()));
        assertThat(newSession[0].isOpen(), is(true));

        // and when client thread continues
        newSessionAcceptedCountDownLatch.countDown();
        sessionClosedCountdownLatch.await();
        client.stop();

        // then verify some stuff
        assertThat(client.isRunning(), is(false)); // must be stopped already
        assertThat(newSession[0], is(sameInstance(closedSession[0])));
        assertThat(newSession[0].isClosed(), is(true));

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openClientSocket(HOST, PORT);
        verify(selectorMock, times(1)).register(socketMock, false);
        verify(selectorMock, times(1)).close();
        verify(socketMock, times(1)).close();
        verify(selectorMock, times(0)).select();
    }

    @Test
    public void clientStopsWhenModifyingSelectorFails() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openClientSocket(anyString(), anyInt())).thenReturn(socketMock);
        doThrow(IOException.class).when(selectorMock).modify(ArgumentMatchers.any(TCPClientSocket.class), anyBoolean());

        final CountDownLatch sessionClosedCountdownLatch = new CountDownLatch(1);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                sessionClosedCountdownLatch.countDown();

                return null;
            }
        }).when(sessionEventHandlerMock).sessionClosed(ArgumentMatchers.any(Session.class));

        // when
        boolean obtained = client.connect(HOST, PORT);
        sessionClosedCountdownLatch.await();
        client.stop();

        // then verify some stuff
        assertThat(obtained, is(true));
        assertThat(client.isRunning(), is(false)); // must be stopped already

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openClientSocket(HOST, PORT);
        verify(selectorMock, times(1)).register(socketMock, false);
        verify(selectorMock, times(1)).modify(socketMock, false);
        verify(selectorMock, times(1)).close();
        verify(socketMock, times(1)).close();
        verify(selectorMock, times(0)).select();
    }

    @Test
    public void clientStopsWhenSelectFails() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openClientSocket(anyString(), anyInt())).thenReturn(socketMock);
        doThrow(IOException.class).when(selectorMock).select();

        final CountDownLatch sessionClosedCountdownLatch = new CountDownLatch(1);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                sessionClosedCountdownLatch.countDown();

                return null;
            }
        }).when(sessionEventHandlerMock).sessionClosed(ArgumentMatchers.any(Session.class));

        // when
        boolean obtained = client.connect(HOST, PORT);
        sessionClosedCountdownLatch.await();
        client.stop();

        // then verify some stuff
        assertThat(obtained, is(true));
        assertThat(client.isRunning(), is(false)); // must be stopped already

        // verify method calls
        verify(socketFactoryMock, times(1)).openSocketSelector();
        verify(socketFactoryMock, times(1)).openClientSocket(HOST, PORT);
        verify(selectorMock, times(1)).register(socketMock, false);
        verify(selectorMock, times(1)).modify(socketMock, false);
        verify(selectorMock, times(1)).select();
        verify(selectorMock, times(1)).close();
        verify(socketMock, times(1)).close();
    }

    @Test
    public void clientWritesDataToSocket() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openClientSocket(anyString(), anyInt())).thenReturn(socketMock);
        doReturn(selectionResultMock).doThrow(IOException.class).when(selectorMock).select();
        doReturn(Collections.singleton(socketMock)).when(selectionResultMock).getWritableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getReadableSockets();

        final CountDownLatch sessionClosedCountdownLatch = new CountDownLatch(1);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                // immediately put some data into session on open
                Session session = invocation.getArgument(0);
                session.write("Hello World!".getBytes());

                return null;
            }
        }).when(sessionEventHandlerMock).newSession(ArgumentMatchers.any(Session.class));

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                sessionClosedCountdownLatch.countDown();

                return null;
            }
        }).when(sessionEventHandlerMock).sessionClosed(ArgumentMatchers.any(Session.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                ByteBuffer data = invocation.getArgument(0);
                byte[] consumed = new byte[data.remaining()];
                data.get(consumed);

                return consumed.length;
            }
        }).when(socketMock).write(ArgumentMatchers.any(ByteBuffer.class));

        // when
        boolean obtained = client.connect(HOST, PORT);
        sessionClosedCountdownLatch.await();
        client.stop();

        // then verify some stuff
        assertThat(obtained, is(true));
        assertThat(client.isRunning(), is(false)); // must be stopped already

        // verify method calls
        verify(selectorMock, times(1)).modify(socketMock, true);
        verify(selectorMock, times(1)).modify(socketMock, false);
        verify(selectorMock, times(2)).select();
        verify(selectorMock, times(1)).close();
        verify(socketMock, times(1)).close();
        verify(sessionEventHandlerMock, times(1)).newSession(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(1)).sessionDataWritten(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataReceived(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(1)).sessionClosed(ArgumentMatchers.any(Session.class));
        verify(selectionResultMock, times(1)).writeHandled(socketMock);
        verify(selectionResultMock, times(0)).readHandled(any(TCPClientSocket.class));
    }

    @Test
    public void whenWritingToSocketThrowsException() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openClientSocket(anyString(), anyInt())).thenReturn(socketMock);
        doReturn(selectionResultMock).when(selectorMock).select();
        doReturn(Collections.singleton(socketMock)).when(selectionResultMock).getWritableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getReadableSockets();

        final CountDownLatch sessionClosedCountdownLatch = new CountDownLatch(1);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                // immediately put some data into session on open
                Session session = invocation.getArgument(0);
                session.write("Hello World!".getBytes());

                return null;
            }
        }).when(sessionEventHandlerMock).newSession(ArgumentMatchers.any(Session.class));

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                sessionClosedCountdownLatch.countDown();

                return null;
            }
        }).when(sessionEventHandlerMock).sessionClosed(ArgumentMatchers.any(Session.class));

        doThrow(IOException.class).when(socketMock).write(ArgumentMatchers.any(ByteBuffer.class));

        // when
        boolean obtained = client.connect(HOST, PORT);
        sessionClosedCountdownLatch.await();
        client.stop();

        // then verify some stuff
        assertThat(obtained, is(true));
        assertThat(client.isRunning(), is(false)); // must be stopped already

        // verify method calls
        verify(selectorMock, times(1)).modify(socketMock, true);
        verify(selectorMock, times(1)).select();
        verify(selectorMock, times(1)).close();
        verify(socketMock, times(1)).close();
        verify(sessionEventHandlerMock, times(1)).newSession(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataWritten(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataReceived(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(1)).sessionClosed(ArgumentMatchers.any(Session.class));
        verify(selectionResultMock, times(1)).writeHandled(socketMock);
        verify(selectionResultMock, times(0)).readHandled(any(TCPClientSocket.class));
    }

    @Test
    public void whenWritingToSocketDoesNotWriteAnything() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openClientSocket(anyString(), anyInt())).thenReturn(socketMock);
        doReturn(selectionResultMock).doThrow(IOException.class).when(selectorMock).select();
        doReturn(Collections.singleton(socketMock)).when(selectionResultMock).getWritableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getReadableSockets();

        final CountDownLatch sessionClosedCountdownLatch = new CountDownLatch(1);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                // immediately put some data into session on open
                Session session = invocation.getArgument(0);
                session.write("Hello World!".getBytes());

                return null;
            }
        }).when(sessionEventHandlerMock).newSession(ArgumentMatchers.any(Session.class));

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                sessionClosedCountdownLatch.countDown();

                return null;
            }
        }).when(sessionEventHandlerMock).sessionClosed(ArgumentMatchers.any(Session.class));

        doReturn(0).when(socketMock).write(ArgumentMatchers.any(ByteBuffer.class));

        // when
        boolean obtained = client.connect(HOST, PORT);
        sessionClosedCountdownLatch.await();
        client.stop();

        // then verify some stuff
        assertThat(obtained, is(true));
        assertThat(client.isRunning(), is(false)); // must be stopped already

        // verify method calls
        verify(selectorMock, times(2)).modify(socketMock, true);
        verify(selectorMock, times(2)).select();
        verify(selectorMock, times(1)).close();
        verify(socketMock, times(1)).close();
        verify(sessionEventHandlerMock, times(1)).newSession(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataWritten(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataReceived(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(1)).sessionClosed(ArgumentMatchers.any(Session.class));
        verify(selectionResultMock, times(1)).writeHandled(socketMock);
        verify(selectionResultMock, times(0)).readHandled(any(TCPClientSocket.class));
    }

    @Test
    public void clientReadsDataFromSocket() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openClientSocket(anyString(), anyInt())).thenReturn(socketMock);
        doReturn(selectionResultMock).doThrow(IOException.class).when(selectorMock).select();
        doReturn(Collections.singleton(socketMock)).when(selectionResultMock).getReadableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getWritableSockets();

        final CountDownLatch sessionClosedCountdownLatch = new CountDownLatch(1);
        final List<byte[]> receivedData = new LinkedList<>();

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                sessionClosedCountdownLatch.countDown();

                return null;
            }
        }).when(sessionEventHandlerMock).sessionClosed(ArgumentMatchers.any(Session.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Session session = invocation.getArgument(0);
                receivedData.add(session.read());

                return null;
            }
        }).when(sessionEventHandlerMock).sessionDataReceived(ArgumentMatchers.any(Session.class));

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {

                byte[] consumed = "Hello World!".getBytes();
                ByteBuffer data = invocation.getArgument(0);
                data.put(consumed);

                return consumed.length;
            }
        }).when(socketMock).read(ArgumentMatchers.any(ByteBuffer.class));

        // when
        boolean obtained = client.connect(HOST, PORT);
        sessionClosedCountdownLatch.await();
        client.stop();

        // then verify some stuff
        assertThat(obtained, is(true));
        assertThat(client.isRunning(), is(false)); // must be stopped already
        assertThat(receivedData.size(), is(1));
        assertThat(receivedData.get(0), is(equalTo("Hello World!".getBytes())));

        // verify method calls
        verify(selectorMock, times(2)).modify(socketMock, false);
        verify(selectorMock, times(2)).select();
        verify(selectorMock, times(1)).close();
        verify(socketMock, times(1)).close();
        verify(sessionEventHandlerMock, times(1)).newSession(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataWritten(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(1)).sessionDataReceived(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(1)).sessionClosed(ArgumentMatchers.any(Session.class));
        verify(selectionResultMock, times(0)).writeHandled(socketMock);
        verify(selectionResultMock, times(1)).readHandled(any(TCPClientSocket.class));
    }

    @Test
    public void whenReadingDataFromSocketThrowsException() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openClientSocket(anyString(), anyInt())).thenReturn(socketMock);
        doReturn(selectionResultMock).when(selectorMock).select();
        doReturn(Collections.singleton(socketMock)).when(selectionResultMock).getReadableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getWritableSockets();

        final CountDownLatch sessionClosedCountdownLatch = new CountDownLatch(1);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                sessionClosedCountdownLatch.countDown();

                return null;
            }
        }).when(sessionEventHandlerMock).sessionClosed(ArgumentMatchers.any(Session.class));

        doThrow(IOException.class).when(socketMock).read(ArgumentMatchers.any(ByteBuffer.class));

        // when
        boolean obtained = client.connect(HOST, PORT);
        sessionClosedCountdownLatch.await();
        client.stop();

        // then verify some stuff
        assertThat(obtained, is(true));
        assertThat(client.isRunning(), is(false)); // must be stopped already

        // verify method calls
        verify(selectorMock, times(1)).modify(socketMock, false);
        verify(selectorMock, times(1)).select();
        verify(selectorMock, times(1)).close();
        verify(socketMock, times(1)).close();
        verify(sessionEventHandlerMock, times(1)).newSession(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataWritten(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataReceived(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(1)).sessionClosed(ArgumentMatchers.any(Session.class));
        verify(selectionResultMock, times(0)).writeHandled(socketMock);
        verify(selectionResultMock, times(1)).readHandled(any(TCPClientSocket.class));
    }

    @Test
    public void whenReadingDataFromSocketAndSocketIsClosed() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openClientSocket(anyString(), anyInt())).thenReturn(socketMock);
        doReturn(selectionResultMock).when(selectorMock).select();
        doReturn(Collections.singleton(socketMock)).when(selectionResultMock).getReadableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getWritableSockets();

        final CountDownLatch sessionClosedCountdownLatch = new CountDownLatch(1);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                sessionClosedCountdownLatch.countDown();

                return null;
            }
        }).when(sessionEventHandlerMock).sessionClosed(ArgumentMatchers.any(Session.class));

        doReturn(-1).when(socketMock).read(ArgumentMatchers.any(ByteBuffer.class));

        // when
        boolean obtained = client.connect(HOST, PORT);
        sessionClosedCountdownLatch.await();
        client.stop();

        // then verify some stuff
        assertThat(obtained, is(true));
        assertThat(client.isRunning(), is(false)); // must be stopped already

        // verify method calls
        verify(selectorMock, times(1)).modify(socketMock, false);
        verify(selectorMock, times(1)).select();
        verify(selectorMock, times(1)).close();
        verify(socketMock, times(1)).close();
        verify(sessionEventHandlerMock, times(1)).newSession(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataWritten(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataReceived(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(1)).sessionClosed(ArgumentMatchers.any(Session.class));
        verify(selectionResultMock, times(0)).writeHandled(socketMock);
        verify(selectionResultMock, times(1)).readHandled(any(TCPClientSocket.class));
    }

    @Test
    public void whenReadingDataFromSocketReadsNothing() throws IOException, InterruptedException {

        // given
        when(socketFactoryMock.openSocketSelector()).thenReturn(selectorMock);
        when(socketFactoryMock.openClientSocket(anyString(), anyInt())).thenReturn(socketMock);
        doReturn(selectionResultMock).doThrow(IOException.class).when(selectorMock).select();
        doReturn(Collections.singleton(socketMock)).when(selectionResultMock).getReadableSockets();
        doReturn(Collections.emptySet()).when(selectionResultMock).getWritableSockets();

        final CountDownLatch sessionClosedCountdownLatch = new CountDownLatch(1);

        doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                sessionClosedCountdownLatch.countDown();

                return null;
            }
        }).when(sessionEventHandlerMock).sessionClosed(ArgumentMatchers.any(Session.class));

        doReturn(0).when(socketMock).read(ArgumentMatchers.any(ByteBuffer.class));

        // when
        boolean obtained = client.connect(HOST, PORT);
        sessionClosedCountdownLatch.await();
        client.stop();

        // then verify some stuff
        assertThat(obtained, is(true));
        assertThat(client.isRunning(), is(false)); // must be stopped already

        // verify method calls
        verify(selectorMock, times(2)).modify(socketMock, false);
        verify(selectorMock, times(2)).select();
        verify(selectorMock, times(1)).close();
        verify(socketMock, times(1)).close();
        verify(sessionEventHandlerMock, times(1)).newSession(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataWritten(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(0)).sessionDataReceived(ArgumentMatchers.any(Session.class));
        verify(sessionEventHandlerMock, times(1)).sessionClosed(ArgumentMatchers.any(Session.class));
        verify(selectionResultMock, times(0)).writeHandled(socketMock);
        verify(selectionResultMock, times(1)).readHandled(any(TCPClientSocket.class));
    }
}
