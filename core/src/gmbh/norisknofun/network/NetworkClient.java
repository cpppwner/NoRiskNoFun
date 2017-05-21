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

        clientThread = new Thread(new Runnable() {
            @Override
            public void run() {
                runClient();
            }
        });
        clientThread.setName(this.getClass().getSimpleName());
        clientThread.start();

        return true;
    }

    private boolean initNetworking(String host, int port) {

        boolean success = true;
        try {
            selector = socketFactory.openSocketSelector();
            clientSocket = socketFactory.openClientSocket(host, port);
            selector.register(clientSocket, false); // initial registration is read-only
        } catch (IOException e) {
            Gdx.app.log(this.getClass().getSimpleName(), "Failed to connect", e);
            success = false;
        }

        if (success) {
            session = new SessionImpl(selector);
        }

        return success;
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

    private void runClient() {

        // first notify event handler about session creation
        sessionEventHandler.newSession(session);

        while (isRunnable()) {


            SelectionResult selectionResult = select();
            boolean success = selectionResult != null;

            if (success && !selectionResult.getReadableSockets().isEmpty()) {
                success = handleRead();
                selectionResult.readHandled(clientSocket);
            }
            if (success && !selectionResult.getWritableSockets().isEmpty()) {
                success = handleWrite();
                selectionResult.writeHandled(clientSocket);
            }

            if (!success)
                break;
        }

        // last but not least terminate the session & close networking
        terminateSession();
        closeNetworking();
    }

    private SelectionResult select() {

        SelectionResult result = null;
        try {
            selector.modify(clientSocket, session.hasDataToWrite());
            result = selector.select();
        } catch (IOException e) {
            Gdx.app.log(this.getClass().getSimpleName(), "I/O error in client", e);
        }

        return result;
    }

    private boolean isRunnable() {

        if (Thread.interrupted())
            return false;

        return session.isOpen() || session.hasDataToWrite();
    }

    private void terminateSession() {
        if (session.isOpen()) {
            session.terminate();
        }
        sessionEventHandler.sessionClosed(session);
    }


    private boolean handleRead() {

        int numBytesRead;
        try {
            numBytesRead = session.doReadFromSocket(clientSocket);
        } catch (IOException e) {
            Gdx.app.log(this.getClass().getSimpleName(), "I/O exception during socket read", e);
            numBytesRead = -1;
        }

        if (numBytesRead > 0) {
            sessionEventHandler.sessionDataReceived(session);
        }

        return numBytesRead >= 0;
    }

    private boolean handleWrite() {
        int numBytesWritten;
        try {
            numBytesWritten = session.doWriteToSocket(clientSocket);
        } catch (IOException e) {
            Gdx.app.log(this.getClass().getSimpleName(), "I/O exception during socket write", e);
            numBytesWritten = -1;
        }

        if (numBytesWritten > 0) {
            sessionEventHandler.sessionDataWritten(session);
        }

        return numBytesWritten >= 0;
    }

    public synchronized void stop() throws InterruptedException {

        if (!isRunning()) {
            return; // server was not started yet
        }

        clientThread.interrupt();
        clientThread.join();
    }

    public synchronized boolean isRunning() {

        return clientThread != null && clientThread.isAlive();
    }
}
