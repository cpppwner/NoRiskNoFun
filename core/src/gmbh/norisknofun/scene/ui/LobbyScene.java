package gmbh.norisknofun.scene.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import gmbh.norisknofun.assets.AssetSound;
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

    private final SceneData sceneData;

    /**
     * Sound played when buttons are clicked.
     */
    private final AssetSound buttonPressedSound;

    /**
     * Button for starting the game.
     */
    private ImageButtonSceneObject imageButtonStartGame;

    public LobbyScene(SceneData sceneData) {
        super(SceneNames.LOBBY_SCENE, Color.WHITE);
        this.sceneData = sceneData;
        this.buttonPressedSound = sceneData.createSound(Assets.BUTTON_PRESSED_SOUND_FILENAME);

        setBackground();
        initImageButtons();
        initLabel();
    }

    private void setBackground(){

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

        // TODO init lobby scene, as soon as the server accepted our join

        super.render(delta);
    }

    @Override
    public void dispose() {
        buttonPressedSound.dispose();
        super.dispose();
    }
}
