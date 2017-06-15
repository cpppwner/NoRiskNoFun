package gmbh.norisknofun.scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;

import gmbh.norisknofun.assets.AssetFactory;
import gmbh.norisknofun.assets.AssetImageButton;
import gmbh.norisknofun.assets.AssetLabel;
import gmbh.norisknofun.assets.AssetModalDialog;
import gmbh.norisknofun.assets.AssetNumericField;
import gmbh.norisknofun.assets.AssetPixmapTexture;
import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.assets.AssetTextButton;
import gmbh.norisknofun.assets.AssetTextField;
import gmbh.norisknofun.assets.AssetTexture;
import gmbh.norisknofun.assets.FontDescriptor;
import gmbh.norisknofun.assets.ModalDialogDescriptor;
import gmbh.norisknofun.assets.TextButtonDescriptor;
import gmbh.norisknofun.assets.TextFieldDescriptor;
import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.GameServices;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.network.socket.SocketFactory;

/**
 * Data class shared amongst the scenes.
 *
 * <p>
 *     This class is for collecting input from the various scenes + adds some
 *     helpers to create appropriate assets.
 * </p>
 */
public class SceneData {

    private final AssetFactory assetFactory;

    private final GameDataServer gameDataServer;
    private final GameData gameDataClient;
    private final GameServices gameServices;

    /**
     * Initialize SceneData instance.
     *
     * @param assetFactory Factory for creating assets.
     */
    public SceneData(AssetFactory assetFactory, SocketFactory socketFactory) {

        this.assetFactory = assetFactory;

        gameDataServer = new GameDataServer();
        gameDataClient = new GameData();
        gameServices = new GameServices(socketFactory, gameDataServer, gameDataClient);
        gameDataClient.setMapAsset(assetFactory.createAssetMap("maps/Dummy Two.map")); // fixme: termporarily hardcode map
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
     * Create a 1x1 pixels pixmap texture with given color.
     *
     * @param color The color used in the pixmap texture.
     */
    public AssetPixmapTexture createPixmapTexture(Color color) {

        return getAssetFactory().createAssetPixmapTexture(color);
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

    /**
     * Create image button asset.
     *
     * @param textureFilename The texture filename for which to create the image button.
     * @return Newly created image button asset.
     */
    public AssetImageButton createImageButton(String textureFilename) {

        return getAssetFactory().createAssetImageButton(textureFilename);
    }

    /**
     * Create text button asset.
     *
     * @param initialButtonText The initial button text for the button.
     * @param textButtonDescriptor Descriptor container object containing some style information.
     * @return Newly created text button asset.
     */
    public AssetTextButton createTextButton(String initialButtonText, TextButtonDescriptor textButtonDescriptor) {

        return getAssetFactory().createAssetTextButton(initialButtonText, textButtonDescriptor);
    }

    /**
     * Create text input field asset.
     *
     * @param textFieldDescriptor Descriptor container object containing some style information.
     * @return Newly created text input field asset.
     */
    public AssetTextField createTextField(TextFieldDescriptor textFieldDescriptor) {

        return getAssetFactory().createAssetTextField("", textFieldDescriptor);
    }

    /**
     * Create numeric input field asset.
     *
     * <p>
     *     Numeric input fields are basically text input fields with an input filter, that only
     *     allows numeric input values.
     * </p>
     *
     * @param textFieldDescriptor Descriptor container object containing some style information.
     * @return Newly created numeric input field asset.
     */
    public AssetNumericField createNumericField(TextFieldDescriptor textFieldDescriptor) {

        return getAssetFactory().createAssetNumericField(0, textFieldDescriptor);
    }

    /**
     * Create special asset, a modal dialog.
     *
     * <p>
     *     An {@link AssetModalDialog} is a modal dialog with some text shown in the dialog,
     *     and OK button and some title in the dialog menu bar.
     *
     *     Use {@link AssetModalDialog#show(Stage)} to show the dialog and do not add any
     *     SceneObject to the scene's internal scene object list.
     * </p>
     *
     * @param dialogText The text shown in the dialog window.
     * @param modalDialogDescriptor Descriptor describing some style layout.
     * @return Newly created modal dialog asset.
     */
    public AssetModalDialog createModalDialog(String dialogText, ModalDialogDescriptor modalDialogDescriptor) {

        return getAssetFactory().createAssetModalDialog(dialogText, modalDialogDescriptor);
    }

    /**
     * Set boolean flag indicating if the game is also hosted on this machine.
     *
     * @param gameHost {@code true} if game must be hosted, {@code false} otherwise.
     */
    public void setGameHost(boolean gameHost) {
        if (gameHost) {
            gameServices.setHostingMode();
        } else {
            gameServices.setJoiningMode();
        }
    }

    public boolean isGameHost() {
        return gameServices.isHostingMode();
    }

    /**
     * Set maximum number of allowed players - only used when hosting the game.
     */
    public void setMaximumNumberOfPlayers(int maximumNumberOfPlayers) {

        gameDataServer.setMaxPlayer(maximumNumberOfPlayers);
    }

    /**
     * Set map filename.
     */
    public void setMapFilename(String mapFilename) {

        // NOTE: create two instances to avoid race conditions between server & client.
        gameDataServer.setMapFilename(mapFilename);
        gameDataServer.setMapAsset(getAssetFactory().createAssetMap(mapFilename));
    }

    /**
     * Get client game data object.
     */
    public GameData getGameData() {
        return gameDataClient;
    }

    /**
     * Set the player's desired name which he wants to use in the game.
     * @param playerName The name the player chose in the scenes.
     */
    public void setPlayerName(String playerName) {
        gameDataClient.setPlayerName(playerName);
    }

    /**
     * Set the host ip to which the client should connect to.
     *
     * <p>
     *     By default this is preset to the own ip address - when hosting the game.
     * </p>
     *
     * @param hostIp The host ip to which the client should connect.
     */
    public void setHostIp(String hostIp) {
        gameServices.setHostIp(hostIp);
    }

    /**
     * Get the host (our own) IP address.
     *
     * <p>
     *     It's shown in some scene.
     * </p>
     *
     * @return The host's IP address.
     */
    public String getHostIp() {
        return gameServices.getHostIp();
    }

    /**
     * Set the last error that occurred.
     *
     * @param lastError The last error messaged.
     */
    public void setLastError(String lastError) {
        gameDataClient.setLastError(lastError);
    }

    /**
     * Get the last error message, if one was set before using {@link SceneData#setLastError(String)}.
     *
     * @return The last error message that was set or {@code null} if none was set.
     */
    public String getLastError() {
        return gameDataClient.getLastError();
    }

    /**
     * Start game related services.
     *
     * @return {@code true} if services were successfully started, {@code false} otherwise.
     * @throws InterruptedException In case the thread gets interrupted.
     */
    public boolean startGameServices() throws InterruptedException {
        return gameServices.startServices();
    }

    /**
     * Stop game related services.
     *
     * @throws InterruptedException In case the thread gets interrupted.
     */
    public void stopGameServices() throws InterruptedException {
        gameServices.stopServices();
    }

    /**
     * Send a message from the GUI to the game client's inbound queue.
     *
     * @param message The message to send from GUI.
     */
    public void sendMessageFromGui(Message message) {
        gameServices.sendMessageFromGui(message);
    }

    public void processPendingMessages() {
        gameServices.processPendingMessages();
    }
}
