package gmbh.norisknofun.network.socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.CancelledKeyException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Integration tests to test {@link SocketSelectorImpl}.
 */
public class SocketSelectorImplTests {

    private static final String HOST = "localhost";
    private static final int PORT = 27010;
    private static final int SERVER_SOCKET_TIMEOUT_IN_MILLISECONDS = 60 * 1000; // 60 seconds timeout on server side

    private SocketSelector selector;

    private ServerSocket serverSocket;
    private Thread serverThread;
    private CountDownLatch serverReadyLatch;

    @Rule
    public Timeout globalTimeout= new Timeout(1, TimeUnit.MINUTES); // each test gets a timeout of 1 min

    @Before
    public void setUp() throws IOException {

        selector = SocketSelectorImpl.open();

        serverSocket = null;
        serverReadyLatch = new CountDownLatch(1);
        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    List<Socket> clients = new LinkedList<>();

                    serverSocket = new ServerSocket();
                    serverSocket.setReuseAddress(true);
                    serverSocket.setSoTimeout(SERVER_SOCKET_TIMEOUT_IN_MILLISECONDS);
                    serverSocket.bind(new InetSocketAddress(HOST, PORT));

                    serverReadyLatch.countDown();

                    while (!Thread.currentThread().isInterrupted()) {

                        try {
                            clients.add(serverSocket.accept());
                        } catch (Exception e) {
                            // intentionally left empty
                        }

                    }

                    for (Socket socket : clients) {
                        socket.close();
                    }
                    serverSocket.close();

                } catch (IOException e) {
                    fail(e.getMessage());
                }
            }
        });
    }

    @After
    public void tearDown() throws IOException {

        if (selector != null) {
            selector.close();
        }

        if (serverThread.isAlive()) {
            serverThread.interrupt();
            try {
                serverSocket.close();
            } catch (Exception e) {
                // intentionally left empty
            }
            try {
                serverThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void registerAndUnregisterTCPServerSocket() {

        // open up server sockets
        TCPServerSocket socketOne = null;
        TCPServerSocket socketTwo = null;

        try {
            socketOne = TCPServerSocketImpl.open(27001);
            socketTwo = TCPServerSocketImpl.open(27002);
        } catch (IOException e) {
            fail("IOException during server socket open");
        }

        // register both of them
        try {
            selector.register(socketOne);
            selector.register(socketTwo);
        } catch (IOException e) {
            fail("IOException not expected");
        }

        // re-register sockets
        try {
            selector.register(socketOne);
            selector.register(socketTwo);
        } catch (IOException e) {
            fail("IOException not expected");
        }

        // unregister sockets
        selector.unregister(socketOne);
        selector.unregister(socketTwo);

        // unregister sockets again
        selector.unregister(socketOne);
        selector.unregister(socketTwo);

        // close sockets
        try {
            socketOne.close();
            socketTwo.close();
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void registerModifyUnregisterClientSocket() throws InterruptedException {

        // startup server
        serverThread.start();
        serverReadyLatch.await();

        TCPClientSocketImpl clientSocketOne = null;
        TCPClientSocketImpl clientSocketTwo = null;
        try {
            clientSocketOne = TCPClientSocketImpl.open(HOST, PORT);
            clientSocketTwo = TCPClientSocketImpl.open(HOST, PORT);
        } catch (IOException e) {
            fail("IOException not expected");
        }

        // register both of them
        try {
            selector.register(clientSocketOne, true);
            selector.register(clientSocketTwo, false);
        } catch (IOException e) {
            fail("IOException not expected");
        }

        // re-register sockets
        try {
            selector.register(clientSocketOne, false);
            selector.register(clientSocketTwo, true);
        } catch (IOException e) {
            fail("IOException not expected");
        }

        // modify sockets, set both to writable
        try {
            selector.modify(clientSocketOne, true);
            selector.modify(clientSocketTwo, true);
        } catch (IOException e) {
            fail("IOException not expected");
        }

        // modify sockets, set both to not-writable
        try {
            selector.modify(clientSocketOne, false);
            selector.modify(clientSocketTwo, false);
        } catch (IOException e) {
            fail("IOException not expected");
        }

        // unregister sockets
        selector.unregister(clientSocketOne);
        selector.unregister(clientSocketTwo);

        // unregister sockets again
        selector.unregister(clientSocketOne);
        selector.unregister(clientSocketTwo);

        // modify sockets, set both to writable
        try {
            selector.modify(clientSocketOne, true);
            fail("IOException expected"); // key has been canceled
        } catch (CancelledKeyException e) {
            // intentionally left empty
        } catch (IOException e) {
            fail("IOException not expected");
        }

        try {
            clientSocketOne.close();
            clientSocketTwo.close();
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void selectGivesEmptyResultOnWakeup() throws InterruptedException {

        // startup server
        serverThread.start();
        serverReadyLatch.await();

        TCPClientSocketImpl clientSocketOne = null;
        TCPClientSocketImpl clientSocketTwo = null;
        try {
            clientSocketOne = TCPClientSocketImpl.open(HOST, PORT);
            clientSocketTwo = TCPClientSocketImpl.open(HOST, PORT);
        } catch (IOException e) {
            fail("IOException not expected");
        }

        // register both of them
        try {
            selector.register(clientSocketOne, true);
            selector.register(clientSocketTwo, true);
        } catch (IOException e) {
            fail("IOException not expected");
        }

        // open up server sockets
        TCPServerSocket socketOne = null;
        TCPServerSocket socketTwo = null;

        try {
            socketOne = TCPServerSocketImpl.open(27001);
            socketTwo = TCPServerSocketImpl.open(27002);
        } catch (IOException e) {
            fail("IOException during server socket open");
        }

        // register both of them
        try {
            selector.register(socketOne);
            selector.register(socketTwo);
        } catch (IOException e) {
            fail("IOException not expected");
        }

        // first select - since client sockets are writable expect them in the result
        SelectionResult result = null;
        try {
            result = selector.select();
        } catch (IOException e) {
            fail("IOException not expected");
        }

        Set<TCPClientSocket> expectedWritableSockets = new HashSet<TCPClientSocket>(Arrays.asList(clientSocketOne, clientSocketTwo));
        assertThat(result, is(notNullValue()));
        assertThat(result.getAcceptableSockets(), is(equalTo(Collections.<TCPServerSocket>emptySet())));
        assertThat(result.getReadableSockets(), is(equalTo(Collections.<TCPClientSocket>emptySet())));
        assertThat(result.getWritableSockets(), is(equalTo(expectedWritableSockets)));

        // modify client sockets to readonly
        try {
            selector.modify(clientSocketOne, false);
            selector.modify(clientSocketTwo, false);
        } catch (IOException e) {
            fail("IOException not expected");
        }

        // call to wakeup so that next select does not block at all
        selector.wakeup();

        try {
            result = selector.select();
        } catch (IOException e) {
            fail("IOException not expected");
        }

        assertThat(result, is(notNullValue()));
        assertThat(result.getAcceptableSockets(), is(equalTo(Collections.<TCPServerSocket>emptySet())));
        assertThat(result.getReadableSockets(), is(equalTo(Collections.<TCPClientSocket>emptySet())));
        assertThat(result.getWritableSockets(), is(equalTo(Collections.<TCPClientSocket>emptySet())));

        // unregister sockets
        selector.unregister(socketOne);
        selector.unregister(socketTwo);

        // unregister sockets
        selector.unregister(clientSocketOne);
        selector.unregister(clientSocketTwo);

        // close sockets
        try {
            clientSocketOne.close();
            clientSocketTwo.close();
            socketOne.close();
            socketTwo.close();
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void modifyNotRegisteredClientSocket() throws InterruptedException {

        // no exception is expected

        // startup server
        serverThread.start();
        serverReadyLatch.await();

        TCPClientSocketImpl clientSocketOne = null;
        TCPClientSocketImpl clientSocketTwo = null;
        try {
            clientSocketOne = TCPClientSocketImpl.open(HOST, PORT);
            clientSocketTwo = TCPClientSocketImpl.open(HOST, PORT);
        } catch (IOException e) {
            fail("IOException not expected");
        }

        try {
            selector.modify(clientSocketOne, true);
            selector.modify(clientSocketTwo, false);
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }
}
