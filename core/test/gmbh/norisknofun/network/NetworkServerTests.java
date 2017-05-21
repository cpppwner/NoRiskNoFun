package gmbh.norisknofun.network;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import gmbh.norisknofun.GdxTest;
import gmbh.norisknofun.network.socket.SocketFactory;
import gmbh.norisknofun.network.socket.SocketSelector;
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
    private SocketFactory socketFactoryMock;
    private SessionEventHandler sessionEventHandlerMock;

    @Rule
    public Timeout globalTimeout= new Timeout(1, TimeUnit.MINUTES); // each test gets a timeout of 1 min


    @Before
    public void setUp() {

        selectorMock = mock(SocketSelector.class);
        serverSocketMock = mock(TCPServerSocket.class);
        socketFactoryMock = mock(SocketFactory.class);
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
        });

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
}
