package gmbh.norisknofun.network.socket;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Integration tests for testing the {@link TCPClientSocketImpl}.
 */
public class TCPClientSocketImplTests {

    private static final String HOST = "localhost";
    private static final int PORT = 27001;
    private static final int SERVER_SOCKET_TIMEOUT_IN_MILLISECONDS = 60 * 1000; // 60 seconds timeout on server side

    private static final String MESSAGE_SENT_TO_SERVER = "Hello World!";

    private SocketFactory socketFactory;
    private EchoServer echoServer;

    private TCPClientSocket clientSocket;
    private SocketSelector selector;

    private ByteBuffer writeBuffer;
    private ByteBuffer readBuffer;

    @Rule
    public Timeout globalTimeout= new Timeout(1, TimeUnit.MINUTES); // each test gets a timeout of 1 min

    @Before
    public void setUp() {

        socketFactory = new SocketFactoryImpl();

        writeBuffer = ByteBuffer.wrap(MESSAGE_SENT_TO_SERVER.getBytes());
        readBuffer = ByteBuffer.allocate(MESSAGE_SENT_TO_SERVER.getBytes().length);

        clientSocket = null;
        selector = null;
    }

    @After
    public void tearDown() {

        if (echoServer != null) {
            echoServer.stopServer();
        }

        if (clientSocket != null) {
            try {
                clientSocket.close();
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

    @Test(expected = IOException.class)
    public void connectWithoutRunningServerThrowsException() throws IOException {

        socketFactory.openClientSocket(HOST, PORT);
    }

    @Test
    public void getRemoteAddress() {

        // start server & wait till ready for accept
        // Note: the connection is closed on the server side after accepting the
        // client connection, since it's not possible to interrupt the read operation
        // this is a bug from 2001 - http://bugs.java.com/bugdatabase/view_bug.do?bug_id=4514257
        EchoServerConfig config = new EchoServerConfig();
        config.closeConnectionAfterAccept = true;

        echoServer = new EchoServer(config);
        echoServer.startServer();
        try {
            echoServer.waitTillReadyForAccept();
        } catch (InterruptedException e) {
            fail("InterruptedException not expected");
        }

        // connect client socket
        try {
            clientSocket = socketFactory.openClientSocket(HOST, PORT);
        } catch (IOException e) {
            fail("IOException not expected during open");
        }

        // get remote address
        SocketAddress remoteAddress = null;
        try {
            remoteAddress = clientSocket.getRemoteAddress();
        } catch (IOException e) {
            fail("getRemoteAddress must not throw");
        }

        assertThat(remoteAddress, is(notNullValue()));
        assertThat(remoteAddress, is(instanceOf(InetSocketAddress.class)));
        assertThat(((InetSocketAddress)remoteAddress).getHostName(), is(equalTo(HOST)));
        assertThat(((InetSocketAddress)remoteAddress).getPort(), is(equalTo(PORT)));
    }

    @Test
    public void getLocalAddress() {

        // start server & wait till ready for accept
        // Note: the connection is closed on the server side after accepting the
        // client connection, since it's not possible to interrupt the read operation
        // this is a bug from 2001 - http://bugs.java.com/bugdatabase/view_bug.do?bug_id=4514257
        EchoServerConfig config = new EchoServerConfig();
        config.closeConnectionAfterAccept = true;

        echoServer = new EchoServer(config);
        echoServer.startServer();
        try {
            echoServer.waitTillReadyForAccept();
        } catch (InterruptedException e) {
            fail("InterruptedException not expected");
        }

        // connect client socket
        try {
            clientSocket = socketFactory.openClientSocket(HOST, PORT);
        } catch (IOException e) {
            fail("IOException not expected during open");
        }

        // get remote address
        SocketAddress localAddress = null;
        try {
            localAddress = clientSocket.getLocalAddress();
        } catch (IOException e) {
            fail("getRemoteAddress must not throw");
        }

        assertThat(localAddress, is(notNullValue()));
        assertThat(localAddress, is(instanceOf(InetSocketAddress.class)));
        assertThat(((InetSocketAddress)localAddress).getHostName(), is(equalTo(HOST)));
        assertThat(((InetSocketAddress)localAddress).getPort() > 0, is(true));
        assertThat(((InetSocketAddress)localAddress).getPort() <= 65535, is(true));
    }

    @Test
    public void serverClosesClientSocketAfterAcceptingTheClient() {

        // start server & wait till ready for accept
        EchoServerConfig config = new EchoServerConfig();
        config.closeConnectionAfterAccept = true;

        echoServer = new EchoServer(config);
        echoServer.startServer();
        try {
            echoServer.waitTillReadyForAccept();
        } catch (InterruptedException e) {
            fail("InterruptedException not expected");
        }

        // open selector
        try {
            selector = socketFactory.openSocketSelector();
        } catch (IOException e) {
            fail("IOException not expected during selector open");
        }

        // connect client
        try {
            clientSocket = socketFactory.openClientSocket(HOST, PORT);
        } catch (IOException e) {
            fail("IOException not expected during open");
        }

        // register client socket in selector
        try {
            selector.register(clientSocket, true);
        } catch (IOException e) {
            fail("IOException not expected when registering client socket");
        }

        // wait for server shutdown
        echoServer.stopServer();

        // select after server has stopped
        SelectionResult result = null;
        try {
            result = selector.select();
        } catch (IOException e) {
            fail("IOException not expected when performing select");
        }

        // verify selection result
        assertThat(result, is(not(nullValue())));
        assertThat(result.getAcceptableSockets(), is(equalTo(Collections.emptySet())));
        assertThat(result.getReadableSockets(), is(equalTo(Collections.singleton(clientSocket))));
        assertThat(result.getWritableSockets(), is(equalTo(Collections.singleton(clientSocket))));

        // write to socket should not fail at all
        int writeResult = 0;
        try {
            writeResult = clientSocket.write(writeBuffer);
        } catch (IOException e) {
            fail("IOException not expected when performing write");
        }
        assertThat(writeResult, is(equalTo(12)));

        // since the socket is also readable but closed, this operation will fail
        int readResult = 0;
        try {
            readResult = clientSocket.read(readBuffer);
        } catch (IOException e) {
            fail("IOException not expected when performing read");
        }
        assertThat(readResult, is(equalTo(-1)));

        assertThat(echoServer.exceptionEncounteredDuringServe, is(nullValue()));
    }

    @Test
    public void echoClientRegularOperation() {

        // start server & wait till ready for accept
        echoServer = new EchoServer(new EchoServerConfig());
        echoServer.startServer();
        try {
            echoServer.waitTillReadyForAccept();
        } catch (InterruptedException e) {
            fail("InterruptedException not expected");
        }

        // open selector
        try {
            selector = socketFactory.openSocketSelector();
        } catch (IOException e) {
            fail("IOException not expected during selector open");
        }

        // connect client
        try {
            clientSocket = socketFactory.openClientSocket(HOST, PORT);
        } catch (IOException e) {
            fail("IOException not expected during open");
        }

        // register client socket in selector and disallow write in the first place
        try {
            selector.register(clientSocket, true);
        } catch (IOException e) {
            fail("IOException not expected when registering client socket");
        }

        // select after
        SelectionResult result = null;

        // write to socket should not fail at all
        int writeResult = 0;
        do {
            try {
                result = selector.select();
            } catch (IOException e) {
                fail("IOException not expected when performing select");
            }

            // verify selection result
            assertThat(result, is(not(nullValue())));
            assertThat(result.getAcceptableSockets(), is(equalTo(Collections.emptySet())));
            assertThat(result.getReadableSockets(), is(equalTo(Collections.emptySet())));
            assertThat(result.getWritableSockets(), is(equalTo(Collections.singleton(clientSocket))));

            try {
                writeResult += clientSocket.write(writeBuffer);
            } catch (IOException e) {
                fail("IOException not expected when performing write");
            }

            result.writeHandled(clientSocket);

        } while (writeBuffer.hasRemaining());
        assertThat(writeResult, is(equalTo(MESSAGE_SENT_TO_SERVER.getBytes().length)));

        // modify client selection registration and disallow write
        try {
            selector.modify(clientSocket, false);
        } catch (IOException e) {
            fail("IOException not expected when modifying client socket in selector");
        }

        int readResult = 0;
        do {
            // select operation
            try {
                result = selector.select();
            } catch (IOException e) {
                fail("IOException not expected when performing select");
            }

            // verify selection result
            assertThat(result, is(not(nullValue())));
            assertThat(result.getAcceptableSockets(), is(equalTo(Collections.emptySet())));
            assertThat(result.getReadableSockets(), is(equalTo(Collections.singleton(clientSocket))));
            assertThat(result.getWritableSockets(), is(equalTo(Collections.emptySet())));

            try {
                readResult += clientSocket.read(readBuffer);
            } catch (IOException e) {
                fail("IOException not expected when reading data from socket");
            }

            result.readHandled(clientSocket);

        } while (readResult < MESSAGE_SENT_TO_SERVER.getBytes().length);

        // unregister client socket from selector (not needed, closing the client socket should be sufficient
        selector.unregister(clientSocket);

        // close the client socket
        try {
            clientSocket.close();
        } catch (IOException e) {
            fail("IOException not expected when closing client socket");
        }

        // verify data that was read by client
        readBuffer.flip();
        byte[] dataReadByClient = new byte[MESSAGE_SENT_TO_SERVER.getBytes().length];
        readBuffer.get(dataReadByClient);
        assertThat(dataReadByClient, is(equalTo(MESSAGE_SENT_TO_SERVER.getBytes())));

        // ensure the server got what we want it to
        echoServer.stopServer();
        assertThat(echoServer.messageReceivedByServer, is(equalTo(MESSAGE_SENT_TO_SERVER.getBytes())));
        assertThat(echoServer.exceptionEncounteredDuringServe, is(nullValue()));
    }

    @Test
    public void echoClientRegularByteWiseReadWrite() {

        // start server & wait till ready for accept
        echoServer = new EchoServer(new EchoServerConfig());
        echoServer.startServer();
        try {
            echoServer.waitTillReadyForAccept();
        } catch (InterruptedException e) {
            fail("InterruptedException not expected");
        }

        // open selector
        try {
            selector = socketFactory.openSocketSelector();
        } catch (IOException e) {
            fail("IOException not expected during selector open");
        }

        // connect client
        try {
            clientSocket = socketFactory.openClientSocket(HOST, PORT);
        } catch (IOException e) {
            fail("IOException not expected during open");
        }

        // register client socket in selector and disallow write in the first place
        try {
            selector.register(clientSocket, true);
        } catch (IOException e) {
            fail("IOException not expected when registering client socket");
        }

        // select after
        SelectionResult result = null;

        // write to socket should not fail at all
        int writeResult = 0;
        do {
            try {
                result = selector.select();
            } catch (IOException e) {
                fail("IOException not expected when performing select");
            }

            // verify selection result
            assertThat(result, is(not(nullValue())));
            assertThat(result.getAcceptableSockets(), is(equalTo(Collections.emptySet())));
            assertThat(result.getReadableSockets(), is(equalTo(Collections.emptySet())));
            assertThat(result.getWritableSockets(), is(equalTo(Collections.singleton(clientSocket))));

            ByteBuffer buffer = ByteBuffer.allocate(1);
            buffer.put(writeBuffer.get());
            buffer.flip();

            try {
                writeResult += clientSocket.write(buffer);
            } catch (IOException e) {
                fail("IOException not expected when performing write");
            }

            result.writeHandled(clientSocket);

        } while (writeBuffer.hasRemaining());
        assertThat(writeResult, is(equalTo(MESSAGE_SENT_TO_SERVER.getBytes().length)));

        // modify client selection registration and disallow write
        try {
            selector.modify(clientSocket, false);
        } catch (IOException e) {
            fail("IOException not expected when modifying client socket in selector");
        }

        int readResult = 0;
        do {
            // select operation
            try {
                result = selector.select();
            } catch (IOException e) {
                fail("IOException not expected when performing select");
            }

            // verify selection result
            assertThat(result, is(not(nullValue())));
            assertThat(result.getAcceptableSockets(), is(equalTo(Collections.emptySet())));
            assertThat(result.getReadableSockets(), is(equalTo(Collections.singleton(clientSocket))));
            assertThat(result.getWritableSockets(), is(equalTo(Collections.emptySet())));

            ByteBuffer buffer = ByteBuffer.allocate(1);

            try {
                readResult += clientSocket.read(buffer);
            } catch (IOException e) {
                fail("IOException not expected when reading data from socket");
            }

            buffer.flip();
            readBuffer.put(buffer);

            result.readHandled(clientSocket);

        } while (readResult < MESSAGE_SENT_TO_SERVER.getBytes().length);

        // unregister client socket from selector (not needed, closing the client socket should be sufficient
        selector.unregister(clientSocket);

        // close the client socket
        try {
            clientSocket.close();
        } catch (IOException e) {
            fail("IOException not expected when closing client socket");
        }

        // verify data that was read by client
        readBuffer.flip();
        byte[] dataReadByClient = new byte[MESSAGE_SENT_TO_SERVER.getBytes().length];
        readBuffer.get(dataReadByClient);
        assertThat(dataReadByClient, is(equalTo(MESSAGE_SENT_TO_SERVER.getBytes())));

        // ensure the server got what we want it to
        echoServer.stopServer();
        assertThat(echoServer.messageReceivedByServer, is(equalTo(MESSAGE_SENT_TO_SERVER.getBytes())));
        assertThat(echoServer.exceptionEncounteredDuringServe, is(nullValue()));
    }

    /**
     * Special configuration class for the {@link EchoServer}.
     *
     * <p>
     *     Since this is for testing purposes only, there are no set, get methods, but
     *     direct access to fields.
     * </p>
     */
    private static final class EchoServerConfig {

        // inputs for the server
        boolean closeConnectionAfterAccept = false;
        String messageSentToServer = MESSAGE_SENT_TO_SERVER;
    }

    private static final class EchoServer {

        private final Thread serverThread;
        private final EchoServerConfig config;

        private byte[] messageReceivedByServer;
        private IOException exceptionEncounteredDuringServe;

        private CountDownLatch acceptLatch = new CountDownLatch(1);

        EchoServer(EchoServerConfig config) {
            serverThread = new Thread(this::serveOne);
            this.config = config;
        }

        void startServer() {
            serverThread.start();
        }

        void stopServer() {
            if (!serverThread.isAlive())
                return;

            serverThread.interrupt();
            try {
                serverThread.join();
            } catch (InterruptedException e) {
                // intentionally left empty
            }
        }

        void waitTillReadyForAccept() throws InterruptedException {
            acceptLatch.await();
        }

        private void serveOne() {
            try {
                ServerSocket serverSocket = new ServerSocket();
                serverSocket.setReuseAddress(true);
                serverSocket.setSoTimeout(SERVER_SOCKET_TIMEOUT_IN_MILLISECONDS);
                serverSocket.bind(new InetSocketAddress(HOST, PORT));

                acceptLatch.countDown();

                // accept a new client
                Socket clientSocket = serverSocket.accept();

                if (config.closeConnectionAfterAccept) {
                    clientSocket.close();
                    serverSocket.close();
                    return;
                }

                // read data from client
                byte[] dataReceived = new byte[config.messageSentToServer.getBytes().length];
                int read = 0;
                do {
                    read += clientSocket.getInputStream().read(dataReceived, read, dataReceived.length - read);
                } while (read < dataReceived.length);
                messageReceivedByServer = dataReceived;

                // write data back to client
                clientSocket.getOutputStream().write(dataReceived);

                clientSocket.close();
                serverSocket.close();

            } catch(IOException e) {
                exceptionEncounteredDuringServe = e;
            }
        }
    }
}
