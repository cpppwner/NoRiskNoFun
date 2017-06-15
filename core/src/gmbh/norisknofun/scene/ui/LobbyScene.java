package gmbh.norisknofun.scene.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.gamemessages.gui.StartGameClicked;
import gmbh.norisknofun.scene.Assets;
import gmbh.norisknofun.scene.SceneBase;
import gmbh.norisknofun.scene.SceneData;
import gmbh.norisknofun.scene.SceneManager;
import gmbh.norisknofun.scene.SceneNames;
import gmbh.norisknofun.scene.Texts;
import gmbh.norisknofun.scene.common.BackgroundSceneObject;
import gmbh.norisknofun.scene.common.ImageButtonSceneObject;
import gmbh.norisknofun.scene.common.LabelSceneObject;

/**
 * Scene where players are waiting till the host starts the game.
 */
public class LobbyScene extends SceneBase{

    /**
     * Scene data.
     */
    private final SceneData sceneData;

    /**
     * Sound played when buttons are clicked.
     */
    private final AssetSound buttonPressedSound;

    /**
     * As soon as we are connected to the server, those will be initialized.
     */
    private List<LabelSceneObject> playerNameLabels = null;


    /**
     * Button for starting the game.
     */
    private ImageButtonSceneObject imageButtonStartGame = null;

    public LobbyScene(SceneData sceneData) {
        super(SceneNames.LOBBY_SCENE, Color.WHITE);
        this.sceneData = sceneData;
        this.buttonPressedSound = sceneData.createSound(Assets.BUTTON_PRESSED_SOUND_FILENAME);

        setBackground();
        initImageButtons();
        initHostIpLabel();
        initLabel();
    }

    private void setBackground() {

        addSceneObject(new BackgroundSceneObject(sceneData.getAssetFactory()));
    }

    private void initImageButtons() {

        if (!sceneData.isGameHost()) {
            return; // player is not hosting - don't offer the player to start the game.
        }

        imageButtonStartGame = new ImageButtonSceneObject(sceneData.createImageButton(Assets.START_GAME_BUTTON_FILENAME), buttonPressedSound);
        imageButtonStartGame.setBounds((Gdx.graphics.getWidth() / 2.0f) - 137.5f, Gdx.graphics.getHeight() / 10.0f, 275f, 240f);
        imageButtonStartGame.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneData.sendMessageFromGui(new StartGameClicked());
            }
        });
        addSceneObject(imageButtonStartGame);
        imageButtonStartGame.setDisabled();
    }

    /**
     * Initialise the label shown on main menu.
     */
    private void initLabel() {

        LabelSceneObject sceneObject = new LabelSceneObject(sceneData.createLabel(Texts.SERVER_LOBBY, Assets.FONT_110PX_WHITE_WITH_BORDER));
        addSceneObject(sceneObject);
        sceneObject.setBounds((Gdx.graphics.getWidth() - sceneObject.getWidth()) / 2.0f,
                Gdx.graphics.getHeight() - (sceneObject.getHeight() * 2.0f),
                sceneObject.getWidth(),
                sceneObject.getHeight());

    }

    private void initHostIpLabel() {

        String text = Texts.SERVER_IP_LABEL_PREFIX + sceneData.getHostIp();
        LabelSceneObject sceneObject = new LabelSceneObject(sceneData.createLabel(text, Assets.FONT_36PX_BLACK_NO_BORDER));
        addSceneObject(sceneObject);
        sceneObject.setBounds(10.0f, 10.0f, sceneObject.getWidth(), sceneObject.getHeight());
    }

    @Override
    public void show() {
        // called when the scene is shown
        super.show();
        startServices();
    }

    private void startServices() {
        try {
            if (!sceneData.startGameServices()) {
                // error occurred - set error message and return to main menu
                sceneData.setLastError(Texts.ERROR_STARTING_GAME_SERVICES);
                SceneManager.getInstance().setActiveScene(SceneNames.MAIN_MENU_SCENE);
            } else {
                Gdx.app.log(getClass().getSimpleName(), "Services successfully started");
            }
        } catch (InterruptedException e) {
            Gdx.app.error(getClass().getSimpleName(), "Thread interrupted while starting game services", e);
            Thread.currentThread().interrupt(); // re-interrupt
        }
    }

    @Override
    public void render(float delta) {

        if (sceneData.getGameData().hasPlayersChanged()) {
            if (!isInitialized()) {
                initialize();
            }

            updateConnectedPlayers();
            updateStartGameButton();
            sceneData.getGameData().resetAllPlayersChanged();
        }

        super.render(delta);
    }

    @Override
    public void dispose() {
        buttonPressedSound.dispose();
        super.dispose();
    }

    private boolean isInitialized() {
        return playerNameLabels != null;
    }

    private void initialize() {

        initializePlayerNameLabels();
        updatePlayerNameLabelBounds();

    }

    private void initializePlayerNameLabels() {

        playerNameLabels = new ArrayList<>(sceneData.getGameData().getMaxNumPlayers());
        for (int i = 0; i < sceneData.getGameData().getMaxNumPlayers(); i++) {
            LabelSceneObject label = new LabelSceneObject(sceneData.createLabel(Texts.WAITING_LABEL_TEXT, Assets.FONT_36PX_BLACK_NO_BORDER));
            label.setBackgroundColor(Color.DARK_GRAY);
            label.setTextAlignment(Align.center);
            playerNameLabels.add(label);
            addSceneObject(label);
        }
    }

    private void updatePlayerNameLabelBounds() {

        // width & height of any label
        float width = (Gdx.graphics.getWidth() / 2.0f) - 60f;
        float height = playerNameLabels.get(0).getHeight() + 40f;

        // first row of labels
        float y = (Gdx.graphics.getHeight() / 2.0f);

        playerNameLabels.get(0).setBounds(50, y, width, height);
        playerNameLabels.get(1).setBounds((Gdx.graphics.getWidth() / 2.0f) + 10f, y, width, height);

        // manipulate y-offset for second row
        y -= (height + 40f);
        if (playerNameLabels.size() == 3) {

            playerNameLabels.get(2).setBounds((Gdx.graphics.getWidth() - width) / 2.0f, y, width, height);

        } else if (playerNameLabels.size() == 4) {

            playerNameLabels.get(2).setBounds(50, y, width, height);
            playerNameLabels.get(3).setBounds((Gdx.graphics.getWidth() / 2.0f) + 10f, y, width, height);
        }
    }

    /**
     * Updates the labels
     */
    private void updateConnectedPlayers() {

        List<Player> connectedPlayers = sceneData.getGameData().getPlayers();
        for (int i = 0; i < playerNameLabels.size(); i++) {

            LabelSceneObject label = playerNameLabels.get(i);
            if (i < connectedPlayers.size()) {
                Player player = connectedPlayers.get(i);
                label.setText(player.getPlayerName());
                label.setBackgroundColor(new Color(player.getColor()));
            } else {
                label.setText(Texts.WAITING_LABEL_TEXT);
                label.setBackgroundColor(Color.DARK_GRAY);
            }
        }
    }

    private void updateStartGameButton() {

        if (imageButtonStartGame == null) {
            // not the host
            return;
        }

        // at least two players are required in order to start the game
        if (sceneData.getGameData().getPlayers().size() < 2) {
            imageButtonStartGame.setDisabled();
        } else {
            imageButtonStartGame.setEnabled();
        }
    }
}
