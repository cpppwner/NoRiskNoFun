package gmbh.norisknofun.scene;

import gmbh.norisknofun.assets.AssetFactory;
import gmbh.norisknofun.assets.AssetImageButton;
import gmbh.norisknofun.assets.AssetLabel;
import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.assets.AssetTextButton;
import gmbh.norisknofun.assets.AssetTextField;
import gmbh.norisknofun.assets.AssetTexture;
import gmbh.norisknofun.assets.FontDescriptor;
import gmbh.norisknofun.assets.TextButtonDescriptor;
import gmbh.norisknofun.assets.TextFieldDescriptor;
import gmbh.norisknofun.game.GameClient;
import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.GameServer;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.server.networking.SessionEventListener;
import gmbh.norisknofun.game.server.networking.SessionEventListenerImpl;
import gmbh.norisknofun.network.NetworkClient;
import gmbh.norisknofun.network.NetworkServer;
import gmbh.norisknofun.network.socket.SocketFactory;

/**
 * Data class shared amongst the scenes.
 */
public class SceneData {

    private static final String LOCALHOST = "127.0.0.1";
    private static final int PORT = 27010;

    private final AssetFactory assetFactory;

    private boolean gameHost = false;
    private String mapFilename = null;
    private String playername = null;
    private String hostOrIp = LOCALHOST;

    private final SocketFactory socketFactory;

    private GameServer gameServer;
    private NetworkServer networkServer;
    private GameDataServer gameDataServer;

    private GameClient gameClient;
    private NetworkClient networkClient;
    private GameData gameDataClient;

    /**
     * Initialize SceneData instance.
     *
     * @param assetFactory Factory for creating assets.
     */
    public SceneData(AssetFactory assetFactory, SocketFactory socketFactory) {

        this.assetFactory = assetFactory;
        this.socketFactory = socketFactory;

        this.gameDataServer = new GameDataServer();
        this.gameDataClient = new GameData();


    }

    /**
     * Get the {@link AssetFactory} passed in the constructor.
     */
    public AssetFactory getAssetFactory() {

        return assetFactory;
    }

    /**
     * Create a texture asset.
     *
     * @param filename The texture filename.
     */
    public AssetTexture createTexture(String filename) {

        return getAssetFactory().createAssetTexture(filename);
    }

    /**
     * Create a sound asset.
     *
     * @param filename The sound filename.
     */
    public AssetSound createSound(String filename) {

        return getAssetFactory().createAssetSound(filename);
    }

    /**
     * Create label asset.
     *
     * @param text The label text.
     * @param fontDescriptor The label font descriptor.
     */
    public AssetLabel createLabel(String text, FontDescriptor fontDescriptor) {

        return getAssetFactory().createAssetLabel(text, fontDescriptor);
    }

    public AssetImageButton createImageButton(String textureFilename) {

        return getAssetFactory().createAssetImageButton(textureFilename);
    }

    public AssetTextButton createTextButton(String initialButtonText, TextButtonDescriptor textButtonDescriptor) {

        return getAssetFactory().createAssetTextButton(initialButtonText, textButtonDescriptor);
    }

    public AssetTextField createTextField(TextFieldDescriptor textFieldDescriptor) {

        return getAssetFactory().createAssetTextField("", textFieldDescriptor);
    }

    /**
     * Set boolean flag indicating if the game is also hosted on this machine.
     *
     * @param gameHost {@code true} if game must be hosted, {@code false} otherwise.
     */
    public void setGameHost(boolean gameHost) {
        this.gameHost = gameHost;
    }

    /**
     * Get boolean flag indicating whether to host the game {@code true}, or just join {@code false}
     */
    public boolean isGameHost() {
        return gameHost;
    }

    /**
     * Set maximum number of allowed players - only used when hosting the game.
     */
    public void setMaximumNumberOfPlayers(int maximumNumberOfPlayers) {

        gameDataServer.setMaxPlayer(maximumNumberOfPlayers);
    }

    /**
     * Get maximum number of allowed players.
     */
    public int getMaximumNumberOfPlayers() {
        return gameDataServer.getMaxPlayer();
    }

    /**
     * Set map filename.
     */
    public void setMapFilename(String mapFilename) {

        // NOTE: create two instances to avoid race conditions between server & client.
        gameDataServer.setMapAsset(getAssetFactory().createAssetMap(mapFilename));
        gameDataClient.setMapAsset(getAssetFactory().createAssetMap(mapFilename));
        this.mapFilename = mapFilename;
    }

    /**
     * Get client game data object.
     */
    public GameData getGameData() {
        return gameDataClient;
    }

    /**
     * Get map filename.
     */
    public String getMapFilename() {
        return mapFilename;
    }

    /**
     * Send a message from the GUI to the game client's inbound queue.
     *
     * @param message The message to send from GUI.
     */
    public void sendMessageFromGui(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("message is null");
        }
        if (gameClient == null) {
            throw new IllegalStateException("Client not started yet");
        }
        gameClient.processGuiMessage(message);
    }

    public boolean startServices() throws InterruptedException {

        if (isGameHost() && gameServer != null) {
            // already started
            throw new IllegalStateException("game server already started");
        }

        if (gameClient != null) {
            throw new IllegalStateException("game client already started");
        }

        boolean success = false;
        if (isGameHost()) {
            success = startServer();
        }

        if (success) {
            success = startClient();
        }

        if (!success) {
            // either server or client could not be started - stop everyting
            stopServices();
        }

        return success;
    }

    private boolean startServer() {

        SessionEventListener serverSessionEventListener = new SessionEventListenerImpl();
        gameServer = new GameServer(gameDataServer, serverSessionEventListener);
        if (!gameServer.start())
            return false;

        networkServer = new NetworkServer(socketFactory, serverSessionEventListener);
        return networkServer.start(PORT);
    }


    private boolean startClient() {

        gameClient = new GameClient(gameDataClient);
        networkClient = new NetworkClient(socketFactory, gameClient);
        return networkClient.connect(hostOrIp, PORT);
    }

    public void stopServices() throws InterruptedException {

        stopClient();
        stopServer();
    }

    private void stopClient() throws InterruptedException {

        if (networkClient != null) {
            networkClient.stop();
        }
        networkClient = null;
    }

    private void stopServer() throws InterruptedException {

        if (networkServer != null) {
            networkServer.stop();
        }
        if (gameServer != null) {
            gameServer.stop();
        }
        networkServer = null;
        gameServer = null;
    }
}
