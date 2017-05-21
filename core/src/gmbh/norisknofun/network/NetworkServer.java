package gmbh.norisknofun.network;

import com.badlogic.gdx.Gdx;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import gmbh.norisknofun.network.socket.SelectionResult;
import gmbh.norisknofun.network.socket.SocketFactory;
import gmbh.norisknofun.network.socket.SocketSelector;
import gmbh.norisknofun.network.socket.TCPClientSocket;
import gmbh.norisknofun.network.socket.TCPServerSocket;

/**
 * Networking server used for the NoRiskNoFun game.
 */
public class NetworkServer {

    private final SocketFactory socketFactory;
    private final SessionEventHandler sessionEventHandler;

    private TCPServerSocket serverSocket = null;
    private SocketSelector selector = null;

    private Thread serverThread = null;

    private final Map<TCPClientSocket, SessionImpl> socketSessionMap = new HashMap<>();

    public NetworkServer(SocketFactory socketFactory, SessionEventHandler sessionEventHandler) {
        this.socketFactory = socketFactory;
        this.sessionEventHandler = sessionEventHandler;
    }

    public synchronized boolean start(int listeningPort) {

        if (!initNetworking(listeningPort)) {
            closeNetworking();
            return false;
        }

        serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                serve();
            }
        });
        serverThread.setName(this.getClass().getSimpleName());
        serverThread.start();

        return true;
    }

    private boolean initNetworking(int listeningPort) {

        try {
            selector = socketFactory.openSocketSelector();
            serverSocket = socketFactory.openServerSocket(listeningPort);
            selector.register(serverSocket);
        } catch (IOException e) {
            Gdx.app.log(this.getClass().getSimpleName(), "Failed to start network server", e);
            return false;
        }

        return true;
    }

    private void closeNetworking() {

        if (serverSocket != null) {
            try {
                serverSocket.close();
                serverSocket = null;
            } catch (Exception e) {
                Gdx.app.log(this.getClass().getSimpleName(), "Failed to close server socket", e);
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

    private void serve() {

        while (!Thread.interrupted()) {
            modifyClientConnections();
            SelectionResult result;
            try {
                result = selector.select();
            } catch (IOException e) {
                Gdx.app.log(this.getClass().getSimpleName(), "Error in select", e);
                break;
            }

            acceptNewConnections(result);
            handleRead(result);
            handleWrite(result);
        }

        // last but not least terminate all accepted client sessions & server networking
        terminateAllClientSessions();
        closeNetworking();
    }

    private void modifyClientConnections() {

        List<TCPClientSocket> sessionsToTerminate = new LinkedList<>();

        for (Map.Entry<TCPClientSocket, SessionImpl> entry : socketSessionMap.entrySet()) {
            if (!entry.getValue().isOpen() && !entry.getValue().hasDataToWrite()) {
                sessionsToTerminate.add(entry.getKey()); // collect all sessions to terminate
            }

            try {
                // modify interest ops
                selector.modify(entry.getKey(), entry.getValue().hasDataToWrite());
            } catch (IOException e) {
                Gdx.app.log(this.getClass().getSimpleName(), "Error while modifying interest ops", e);
                sessionsToTerminate.add(entry.getKey());
            }
        }

        // terminate all previously collected connections/sessions
        for (TCPClientSocket session : sessionsToTerminate) {
            terminateSessionForSocket(session);
        }
    }

    private void terminateAllClientSessions() {

        for (Map.Entry<TCPClientSocket, SessionImpl> entry : socketSessionMap.entrySet()) {
            entry.getValue().terminate();
            selector.unregister(entry.getKey());
            try {
                entry.getKey().close();
            } catch (IOException e) {
                Gdx.app.log(this.getClass().getSimpleName(), "Error while closing client connection", e);
            }
            sessionEventHandler.sessionClosed(entry.getValue());
        }

        socketSessionMap.clear();
    }

    private void acceptNewConnections(SelectionResult result) {

        if (result.getAcceptableSockets().isEmpty())
            return;

        acceptNewConnections();
        result.acceptHandled(serverSocket);
    }

    private void acceptNewConnections() {

        TCPClientSocket clientSocket;
        do {
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                Gdx.app.log(this.getClass().getSimpleName(), "Error in accept", e);
                clientSocket = null;
            }

            if (clientSocket != null) {
                try {
                    selector.register(clientSocket, false);

                    SessionImpl session = new SessionImpl(selector);
                    socketSessionMap.put(clientSocket, session);
                    sessionEventHandler.newSession(session);
                } catch (IOException e) {
                    Gdx.app.log(this.getClass().getSimpleName(), "Could not register client socket in selector", e);
                    try {
                        clientSocket.close();
                    } catch (Exception ex) {
                        // intentionally left empty
                    }
                }
            }
        } while (clientSocket != null);
    }

    private void handleRead(SelectionResult result) {

        for (TCPClientSocket clientSocket : result.getReadableSockets()) {
            handleRead(clientSocket);
            result.readHandled(clientSocket);
        }
    }

    private void handleRead(TCPClientSocket clientSocket) {
        SessionImpl session = socketSessionMap.get(clientSocket);
        int numBytesRead;
        try {
            numBytesRead = session.doReadFromSocket(clientSocket);
        } catch (IOException e) {
            Gdx.app.log(this.getClass().getSimpleName(), "I/O exception during socket read", e);
            numBytesRead = -1; // so that session gets terminated
        }
        if (numBytesRead < 0) {
            // remote site closed the socket
            terminateSessionForSocket(clientSocket);
        }

        if (numBytesRead > 0) {
            sessionEventHandler.sessionDataReceived(session);
        }
    }

    private void handleWrite(SelectionResult result) {

        for (TCPClientSocket clientSocket : result.getWritableSockets()) {
            handleWrite(clientSocket);
            result.writeHandled(clientSocket);
        }
    }

    private void handleWrite(TCPClientSocket clientSocket) {
        SessionImpl session = socketSessionMap.get(clientSocket);
        int numBytesWritten;
        try {
            numBytesWritten = session.doWriteToSocket(clientSocket);
        } catch (IOException e) {
            Gdx.app.log(this.getClass().getSimpleName(), "I/O exception during socket write", e);
            numBytesWritten = -1;
        }
        if (numBytesWritten < 0) {
            // remote site closed the socket
            terminateSessionForSocket(clientSocket);
        }

        if (numBytesWritten > 0) {
            sessionEventHandler.sessionDataWritten(session);
        }
    }

    private void terminateSessionForSocket(TCPClientSocket socket) {

        SessionImpl session = socketSessionMap.remove(socket);
        if (session.isOpen()) {
            session.terminate();
        }
        try {
            socket.close();
        } catch (IOException e) {
            Gdx.app.log(this.getClass().getSimpleName(), "I/O while closing TCPClientSocket", e);
        }
        sessionEventHandler.sessionClosed(session);
    }

    public synchronized void stop() throws InterruptedException {

        if (!isRunning()) {
            return; // server was not started yet
        }

        serverThread.interrupt();
        serverThread.join();
    }

    public synchronized boolean isRunning() {

        return serverThread != null && serverThread.isAlive();
    }
}
