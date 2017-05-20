package gmbh.norisknofun.network;



import com.badlogic.gdx.Gdx;

import java.io.IOException;

import gmbh.norisknofun.network.socket.SelectionResult;
import gmbh.norisknofun.network.socket.SocketFactory;
import gmbh.norisknofun.network.socket.SocketSelector;
import gmbh.norisknofun.network.socket.TCPClientSocket;

/**
 * Created by philipp on 06.04.2017.
 */
public class NetworkClient {

    private final SocketFactory socketFactory;
    private final SessionEventHandler sessionEventHandler;

    private TCPClientSocket clientSocket = null;
    private SocketSelector selector = null;

    private Thread clientThread = null;
    private SessionImpl session = null;

    public NetworkClient(SocketFactory socketFactory, SessionEventHandler sessionEventHandler) {
        this.socketFactory = socketFactory;
        this.sessionEventHandler = sessionEventHandler;
    }

    public synchronized boolean connect(String host, int port) {

        if (!initNetworking(host, port)) {
            closeNetworking();
            return false;
        }

        clientThread = new Thread(this::run);
        clientThread.setName(this.getClass().getSimpleName());
        clientThread.start();

        return true;
    }

    private boolean initNetworking(String host, int port) {

        try {
            selector = socketFactory.openSocketSelector();
            clientSocket = socketFactory.openClientSocket(host, port);
            selector.register(clientSocket, false); // initial registration is read-only
        } catch (IOException e) {
            Gdx.app.log(this.getClass().getSimpleName(), "Failed to connect", e);
            return false;
        }

        session = new SessionImpl(selector);

        return true;
    }

    private void closeNetworking() {

        if (clientSocket != null) {
            try {
                clientSocket.close();
                clientSocket = null;
            } catch (Exception e) {
                Gdx.app.log(this.getClass().getSimpleName(), "Failed to close client socket", e);
            }
        }

        if (selector != null) {
            try {
                selector.close();
                selector = null;
            } catch (Exception e) {
                Gdx.app.log(this.getClass().getSimpleName(), "Failed to close socket selector", e);
            }
        }
    }

    private void run() {

        // first notify event handler about session creation
        sessionEventHandler.newSession(session);

        while (!Thread.interrupted()) {
            if (!session.isOpen() && !session.hasDataToWrite()) {
                break; // just break out and let the rest be handled
            }
            SelectionResult result;
            try {
                selector.modify(clientSocket, session.hasDataToWrite());
                result = selector.select();
            } catch (IOException e) {
                Gdx.app.log(this.getClass().getSimpleName(), "I/O error in client", e);
                break;
            }

            if (!result.getReadableSockets().isEmpty()) {
                // contains only our single socket
                if (!handleRead()) {
                    break;
                }
                result.readHandled(clientSocket);
            }
            if (!result.getWritableSockets().isEmpty()) {
                if (!handleWrite()) {
                    break;
                }
                result.writeHandled(clientSocket);
            }
        }

        // last but not least terminate the session & close networking
        terminateSession();
        closeNetworking();
    }

    private void terminateSession() {
        session.terminate();
        sessionEventHandler.sessionClosed(session);
    }


    private boolean handleRead() {
        int numBytesRead;
        try {
            numBytesRead = session.doReadFromSocket(clientSocket);
        } catch (IOException e) {
            Gdx.app.log(this.getClass().getSimpleName(), "I/O exception during socket read", e);
            return false;
        }
        if (numBytesRead < 0) {
            // remote site closed the socket
            return false;
        }

        if (numBytesRead > 0) {
            sessionEventHandler.sessionDataReceived(session);
        }

        return true;
    }

    private boolean handleWrite() {
        int numBytesWritten;
        try {
            numBytesWritten = session.doWriteToSocket(clientSocket);
        } catch (IOException e) {
            Gdx.app.log(this.getClass().getSimpleName(), "I/O exception during socket write", e);
            return false;
        }
        if (numBytesWritten < 0) {
            // remote site closed the socket
            return false;
        }

        if (numBytesWritten > 0) {
            sessionEventHandler.sessionDataWritten(session);
        }

        return true;
    }

    public synchronized void stop() {

        if (!isRunning()) {
            return; // server was not started yet
        }

        clientThread.interrupt();
        try {
            clientThread.join();
        } catch (InterruptedException e) {
            // intentionally left empty
        }
    }

    public synchronized boolean isRunning() {

        return clientThread != null && clientThread.isAlive();
    }
}
