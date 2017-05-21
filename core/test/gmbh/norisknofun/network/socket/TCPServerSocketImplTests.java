package gmbh.norisknofun.network.socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Integration tests for testing {@link TCPServerSocketImpl} class.
 */
public class TCPServerSocketImplTests {

    private static final String HOST = "localhost";
    private static final int PORT = 27001;

    private SocketFactory socketFactory;
    private TCPServerSocket serverSocket;

    private SocketSelector selector;

    @Rule
    public Timeout globalTimeout= new Timeout(1, TimeUnit.MINUTES); // each test gets a timeout of 1 min

    @Before
    public void setUp() {

        socketFactory = new SocketFactoryImpl();
        serverSocket = null;
        selector = null;
    }

    @After
    public void tearDown() {

        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                // intentionally left empty
            }
        }

        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                // intentionally left empty
            }
        }
    }

    @Test
    public void getLocalAddressWhenBoundViaPortOnly() {

        try {
            serverSocket = socketFactory.openServerSocket(PORT);
        } catch (IOException e) {
            fail("IOException not expected when opening server socket");
        }

        SocketAddress localAddress = null;
        try {
            localAddress = serverSocket.getLocalAddress();
        } catch (IOException e) {
            fail("IOException not expected when getting local address");
        }

        assertThat(localAddress, is(notNullValue()));
        assertThat(localAddress, is(instanceOf(InetSocketAddress.class)));
        assertThat(((InetSocketAddress)localAddress).getPort(), is(equalTo(PORT)));
    }

    @Test
    public void getLocalAddressWhenBoundViaHostnameAndPort() {

        try {
            serverSocket = socketFactory.openServerSocket(HOST, PORT);
        } catch (IOException e) {
            fail("IOException not expected when opening server socket");
        }

        SocketAddress localAddress = null;
        try {
            localAddress = serverSocket.getLocalAddress();
        } catch (IOException e) {
            fail("IOException not expected when getting local address");
        }

        assertThat(localAddress, is(notNullValue()));
        assertThat(localAddress, is(instanceOf(InetSocketAddress.class)));
        assertThat(((InetSocketAddress)localAddress).getHostName(), is(equalTo(HOST)));
        assertThat(((InetSocketAddress)localAddress).getPort(), is(equalTo(PORT)));
    }

    @Test
    public void openServerSocketTwiceOnSameHostnameAndPort() {

        try {
            serverSocket = socketFactory.openServerSocket(HOST, PORT);
        } catch (IOException e) {
            fail("IOException not expected when opening server socket");
        }

        try {
            TCPServerSocket secondServerSocket = socketFactory.openServerSocket(HOST, PORT);
            secondServerSocket.close();
            fail("IOException expected when binding to same port");
        } catch (IOException e) {
            // intentionally left empty
        }
    }

    @Test
    public void acceptWhenThereIsNoAcceptableConnection() {

        try {
            serverSocket = socketFactory.openServerSocket(HOST, PORT);
        } catch (IOException e) {
            fail("IOException not expected when opening server socket");
        }

        try {
            TCPClientSocket obtained = serverSocket.accept();
            assertThat(obtained, is(nullValue()));
        } catch (IOException e) {
            fail("IOException not expected when calling accept");
        }
    }

    @Test
    public void handleAcceptViaSelector() {

        try {
            serverSocket = socketFactory.openServerSocket(HOST, PORT);
        } catch (IOException e) {
            fail("IOException not expected when opening server socket");
        }

        try {
            selector = socketFactory.openSocketSelector();
        } catch(IOException e) {
            fail("IOException not expected when opening selector");
        }

        try {
            selector.register(serverSocket);
        } catch (IOException e) {
            fail("IOException not expected when registering server socket");
        }

        SelectionResult result = null;
        selector.wakeup();
        try {
            result = selector.select();
        } catch (IOException e) {
            fail("IOException not expected when calling select");
        }

        assertThat(result, is(notNullValue()));
        assertThat(result.getAcceptableSockets(), is(equalTo(Collections.emptySet())));


        SimpleClient clientOne = new SimpleClient();
        clientOne.connect();
        SimpleClient clientTwo = new SimpleClient();
        clientTwo.connect();


        try {
            result = selector.select();
        } catch (IOException e) {
            clientOne.stop();
            clientTwo.stop();
            fail("IOException not expected when calling select");
        }

        assertThat(result, is(notNullValue()));
        assertThat(result.getAcceptableSockets(), is(equalTo(Collections.singleton(serverSocket))));

        TCPClientSocket clientConnection = null;
        try {
            clientConnection = serverSocket.accept();
        } catch (IOException e) {
            clientOne.stop();
            clientTwo.stop();
            fail("IOException not expected when calling accept");
        }
        result.acceptHandled(serverSocket);

        assertThat(clientConnection, is(notNullValue()));
        try {
            clientConnection.close();
        } catch (IOException e) {
            clientOne.stop();
            clientTwo.stop();
            fail("IOException not expected when closing client connection");
        }

        try {
            result = selector.select();
        } catch (IOException e) {
            clientOne.stop();
            clientTwo.stop();
            fail("IOException not expected when calling select");
        }

        assertThat(result, is(notNullValue()));
        assertThat(result.getAcceptableSockets(), is(equalTo(Collections.singleton(serverSocket))));

        clientConnection = null;
        try {
            clientConnection = serverSocket.accept();
        } catch (IOException e) {
            clientOne.stop();
            clientTwo.stop();
            fail("IOException not expected when calling accept");
        }
        result.acceptHandled(serverSocket);

        assertThat(clientConnection, is(notNullValue()));
        try {
            clientConnection.close();
        } catch (IOException e) {
            clientOne.stop();
            clientTwo.stop();
            fail("IOException not expected when closing client connection");
        }

        clientOne.stop();
        clientTwo.stop();
    }

    private static final class SimpleClient {

        private Socket socket = null;
        private Thread clientThread = null;
        IOException exceptionInConnect = null;

        void connect() {
            socket = new Socket();

            clientThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        socket.connect(new InetSocketAddress(HOST, PORT));
                    } catch (IOException e) {
                        exceptionInConnect = e;
                    }
                }
            });
            clientThread.start();
        }

        void stop() {
            if (clientThread != null && clientThread.isAlive()) {
                clientThread.interrupt();
                try {
                    clientThread.join();
                } catch (InterruptedException e) {
                    // intentionally left empty
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // intentionally left empty
                }
            }
        }

    }
}
