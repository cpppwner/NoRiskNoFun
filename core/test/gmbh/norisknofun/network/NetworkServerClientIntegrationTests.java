package gmbh.norisknofun.network;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import gmbh.norisknofun.network.socket.SocketFactory;
import gmbh.norisknofun.network.socket.SocketFactoryImpl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Some real integration test, invoking server & client
 */
public class NetworkServerClientIntegrationTests {

    private static final String HOST = "localhost";
    private static final int PORT = 27001;

    private static final int REPEAT_MESSAGE_COUNT = 100;

    private static final String messageOne;
    private static final String messageTwo;

    /**
     * Build up some very long messages
     */
    static {

        StringBuilder messageBuilderOne = new StringBuilder();
        StringBuilder messageBuilderTwo = new StringBuilder();

        for (int i = 0; i < REPEAT_MESSAGE_COUNT; i++) {
            messageBuilderOne.append("Lorem ipsum dolor sit amet, eam no tale solet patrioque, est ")
                .append("ne dico veri. Copiosae petentium no eum, has at wisi dicunt causae. Duo ea ")
                .append("animal eligendi honestatis, dico fastidii officiis sit ne. At oblique ")
                .append("docendi verterem ius, te vide cibo gloriatur nam. Ad has possit delicata. ")
                .append("Sit vocibus accusamus an.");
            messageBuilderTwo.append("Ius an vocent iudicabit. Ut lobortis salutandi honestatis has, ")
                .append("affert alterum tacimates sit eu, primis feugiat lobortis et quo. Ne eum ")
                .append("autem adipisci, labore mentitum suavitate ei qui. Est summo mollis ")
                .append("verterem ne, an tempor tibique lobortis usu. Vide porro principes id usu. ")
                .append("Aeque movet dissentiet ius no, antiopam tractatos signiferumque ad qui, ")
                .append("mucius habemus omittantur an mel.");
        }

        messageOne = messageBuilderOne.toString();
        messageTwo = messageBuilderTwo.toString();
    }

    private NetworkServer server;
    private NetworkClient clientOne;
    private NetworkClient clientTwo;

    private ServerSessionEventHandler serverSessionEventHandler;
    private ClientSessionEventHandler clientSessionEventHandlerOne;
    private ClientSessionEventHandler clientSessionEventHandlerTwo;


    @Rule
    public Timeout globalTimeout= new Timeout(2, TimeUnit.MINUTES); // each test gets a timeout of 1 min

    @Before
    public void setUp() {

        serverSessionEventHandler = new ServerSessionEventHandler();
        clientSessionEventHandlerOne = new ClientSessionEventHandler(messageTwo.getBytes().length * REPEAT_MESSAGE_COUNT);
        clientSessionEventHandlerTwo = new ClientSessionEventHandler(messageOne.getBytes().length * REPEAT_MESSAGE_COUNT);

        SocketFactory socketFactory = new SocketFactoryImpl();
        server = new NetworkServer(socketFactory, serverSessionEventHandler);

        clientOne = new NetworkClient(socketFactory, clientSessionEventHandlerOne);
        clientTwo = new NetworkClient(socketFactory, clientSessionEventHandlerTwo);
    }

    @After
    public void tearDown() throws InterruptedException {

        server.stop();
        clientOne.stop();
        clientTwo.stop();
    }

    @Test
    public void LoremIpsum() throws InterruptedException {

        boolean obtained = server.start(PORT);
        assertThat(obtained, is(true));

        obtained = clientOne.connect(HOST, PORT);
        assertThat(obtained, is(true));

        obtained = clientTwo.connect(HOST, PORT);
        assertThat(obtained, is(true));

        // wait for the sessions
        serverSessionEventHandler.waitForSessions();
        clientSessionEventHandlerOne.waitForSession();
        clientSessionEventHandlerTwo.waitForSession();

        // now start sending the messages
        byte[] dataSentByClientOne = messageOne.getBytes();
        byte[] dataSentByClientTwo = messageTwo.getBytes();

        for (int i = 0; i < REPEAT_MESSAGE_COUNT; i++) {

            clientSessionEventHandlerOne.write(dataSentByClientOne);
            clientSessionEventHandlerTwo.write(dataSentByClientTwo);
        }

        // and wait that both sessions are closed
        clientSessionEventHandlerOne.waitForSessionClosed();
        clientSessionEventHandlerTwo.waitForSessionClosed();
        serverSessionEventHandler.waitForSessionsClosed();

        // and verify the data that was received;
        byte[] sessionOneReceived = clientSessionEventHandlerOne.getReceivedData();
        byte[] sessionTwoReceived = clientSessionEventHandlerTwo.getReceivedData();

        ByteBuffer expectedBufferOne = ByteBuffer.wrap(new byte[dataSentByClientTwo.length * REPEAT_MESSAGE_COUNT]);
        ByteBuffer expectedBufferTwo = ByteBuffer.wrap(new byte[dataSentByClientOne.length * REPEAT_MESSAGE_COUNT]);

        for (int i = 0; i < REPEAT_MESSAGE_COUNT; i++) {

            expectedBufferOne.put(dataSentByClientTwo);
            expectedBufferTwo.put(dataSentByClientOne);
        }

        assertThat(sessionOneReceived, is(equalTo(expectedBufferOne.array())));
        assertThat(sessionTwoReceived, is(equalTo(expectedBufferTwo.array())));
    }

    /**
     * SessionEventHandler that handles the sessions on client side.
     */
    private static final class ClientSessionEventHandler implements SessionEventHandler {

        private final ByteBuffer receiveBuffer;
        private int remainingBytesToRead;

        private Session session;
        private CountDownLatch newSessionHandled = new CountDownLatch(1);
        private CountDownLatch sessionClosedHandled = new CountDownLatch(1);

        ClientSessionEventHandler(int numBytesToRead) {

            receiveBuffer = ByteBuffer.wrap(new byte[numBytesToRead]);
            remainingBytesToRead = numBytesToRead;
        }

        @Override
        public void newSession(Session session) {

            this.session = session;
            newSessionHandled.countDown();
        }

        @Override
        public void sessionClosed(Session session) {

            this.session = null;
            sessionClosedHandled.countDown();
        }

        @Override
        public void sessionDataReceived(Session session) {

            byte[] data = session.read();
            receiveBuffer.put(data);

            remainingBytesToRead -= data.length;

            if (remainingBytesToRead == 0) {
                session.close();
            }
        }

        @Override
        public void sessionDataWritten(Session session) {

            // intentionally left empty
        }

        void waitForSession() throws InterruptedException {

            newSessionHandled.await();
        }

        void waitForSessionClosed() throws InterruptedException {

            sessionClosedHandled.await();
        }

        void write(byte[] data) {

            session.write(data);
        }

        byte[] getReceivedData() {

            return receiveBuffer.array();
        }
    }

    /**
     * SessionEventHandler that handles the sessions on server side.
     */
    private static final class ServerSessionEventHandler implements SessionEventHandler {

        private List<Session> sessions = new ArrayList<>();
        private CountDownLatch newSessionLatch = new CountDownLatch(2);
        private CountDownLatch sessionClosedLatch = new CountDownLatch(2);

        @Override
        public void newSession(Session session) {

            sessions.add(session);
            newSessionLatch.countDown();
        }

        @Override
        public void sessionClosed(Session session) {

            sessions.remove(session);
            sessionClosedLatch.countDown();
        }

        @Override
        public void sessionDataReceived(Session session) {

            // just forward to other session
            byte[] data = session.read();
            if (session == sessions.get(0)) {
                sessions.get(1).write(data);
            } else {
                sessions.get(0).write(data);
            }
        }

        @Override
        public void sessionDataWritten(Session session) {
            // intentionally no code
        }

        void waitForSessions() throws InterruptedException {

            newSessionLatch.await();
        }

        void waitForSessionsClosed() throws InterruptedException {

            sessionClosedLatch.await();
        }
    }
}
