package gmbh.norisknofun.game;

import java.net.InetAddress;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.server.networking.SessionEventListener;
import gmbh.norisknofun.game.server.networking.SessionEventListenerImpl;
import gmbh.norisknofun.network.NetworkClient;
import gmbh.norisknofun.network.NetworkServer;
import gmbh.norisknofun.network.socket.SocketFactory;

/**
 * Class services that are required to play NoRiskNoFun.
 */
public class GameServices {

    private static final String LOCALHOST = InetAddress.getLoopbackAddress().getHostName();
    private static final int PORT = 27010;

    private final NetworkServer networkServer;
    private final GameServer gameServer;

    private final NetworkClient networkClient;
    private final GameClient gameClient;

    private String hostIp = LOCALHOST;
    private boolean hostingMode = true;

    public GameServices(SocketFactory socketFactory, GameDataServer gameDataServer, GameData gameData) {

        SessionEventListener sessionEventListener = new SessionEventListenerImpl();
        networkServer = new NetworkServer(socketFactory, sessionEventListener);
        gameServer = new GameServer(gameDataServer, sessionEventListener);

        gameClient = new GameClient(gameData);
        networkClient = new NetworkClient(socketFactory, gameClient);
    }

    public boolean startServices() throws InterruptedException {

        boolean success = true;
        if (hostingMode && !startServer()) {
            stopServices();
            success = false; // failed to start server - although we are hosting the game
        }

        if (success && !startClient()) {
            stopServices(); // failed to start client
            success = false;
        }

        return success;
    }

    private boolean startServer() {

        return gameServer.start() && networkServer.start(PORT);
    }

    private boolean startClient() {

        gameClient.setInitialState();
        return networkClient.connect(hostIp, PORT);

    }

    public void stopServices() throws InterruptedException {

        stopClient();
        stopServer();
    }

    private void stopClient() throws InterruptedException {

        networkClient.stop();
    }

    private void stopServer() throws InterruptedException {

        networkServer.stop();
        gameServer.stop();
    }

    public void sendMessageFromGui(Message message) {

        if (message == null) {
            throw new IllegalArgumentException("message is null");
        }

        gameClient.processGuiMessage(message);
    }

    public void setHostIp(String hostIp) {
        this.hostIp = hostIp;
    }

    public String getHostIp() {
        return hostIp;
    }

    public void setHostingMode() {

        hostingMode = true;
    }

    public void setJoiningMode() {

        hostingMode = false;
    }

    public boolean isHostingMode() {

        return hostingMode;
    }

    public void processPendingMessages() {
        gameClient.processPendingMessages();
    }
}
