package gmbh.norisknofun.scene.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import gmbh.norisknofun.assets.AssetModalDialog;
import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.scene.Assets;
import gmbh.norisknofun.scene.SceneBase;
import gmbh.norisknofun.scene.SceneData;
import gmbh.norisknofun.scene.SceneNames;
import gmbh.norisknofun.scene.Texts;
import gmbh.norisknofun.scene.common.BackgroundSceneObject;
import gmbh.norisknofun.scene.common.ImageButtonSceneObject;
import gmbh.norisknofun.scene.common.LabelSceneObject;
import gmbh.norisknofun.scene.common.SwitchSceneClickListener;

/**
 * NoRiskNoFun main menu scene.
 */
public final class MainMenuScene extends SceneBase {


    /**
     * Data class shared amongst the scenes.
     */
    private final SceneData sceneData;

    /**
     * Sound played when buttons are clicked.
     */
    private final AssetSound buttonPressedSound;

    /**
     * Construct and initialize main menu scene.
     *
     * @param sceneData Contained class shared amongst scenes.
     */
    public MainMenuScene(SceneData sceneData) {
        super(SceneNames.MAIN_MENU_SCENE, Color.WHITE);
        this.sceneData = sceneData;
        this.buttonPressedSound = sceneData.createSound(Assets.BUTTON_PRESSED_SOUND_FILENAME);

        setBackground();
        initMenuButtons();
        initLabel();
    }

    /**
     * Initialize buttons in the main menu.
     */
    private void initMenuButtons() {

        ImageButtonSceneObject imageButtonCreate = new ImageButtonSceneObject(sceneData.createImageButton(Assets.CREATE_GAME_BUTTON_FILENAME), buttonPressedSound);
        ImageButtonSceneObject imageButtonJoin = new ImageButtonSceneObject(sceneData.createImageButton(Assets.JOIN_GAME_BUTTON_FILENAME), buttonPressedSound);

        imageButtonCreate.setBounds((Gdx.graphics.getWidth() / 6) - 5, (float) (Gdx.graphics.getHeight() / 2.5), 553, 480);
        imageButtonJoin.setBounds((Gdx.graphics.getWidth() / 2) + 10, (float) (Gdx.graphics.getHeight() / 2.5), 553, 480);

        imageButtonCreate.addListener(new SetGameHostClickListener(true));
        imageButtonCreate.addListener(new SwitchSceneClickListener(SceneNames.CREATE_GAME_SCENE));
        imageButtonJoin.addListener(new SetGameHostClickListener(false));
        imageButtonJoin.addListener(new SwitchSceneClickListener(SceneNames.JOIN_GAME_SCENE));

        addSceneObject(imageButtonCreate);
        addSceneObject(imageButtonJoin);
    }

    /**
     * Initialize default background image.
     */
    private void setBackground() {

        addSceneObject(new BackgroundSceneObject(sceneData.getAssetFactory()));
    }

    /**
     * Initialise the label shown on main menu.
     */
    private void initLabel() {

        LabelSceneObject sceneObject = new LabelSceneObject(sceneData.createLabel(Texts.APPLICATION_TITLE, Assets.FONT_110PX_WHITE_WITH_BORDER));
        addSceneObject(sceneObject);
        sceneObject.setBounds((Gdx.graphics.getWidth() - sceneObject.getWidth()) / 2.0f, 160.0f, sceneObject.getWidth(), sceneObject.getHeight());
    }

    @Override
    public void show() {

        super.show();

        String lastError = sceneData.getLastError();
        if (lastError != null) {
            // error occurred and we'll show it
            AssetModalDialog errorDialog = sceneData.createModalDialog(lastError, Assets.ERROR_DIALOG_DESCRIPTOR);
            errorDialog.show(getStage());
            try {
                sceneData.stopGameServices(); // stop game services if any where running
            } catch (InterruptedException e) {
                Gdx.app.error(getClass().getSimpleName(), "Interrupted while stopping game services", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void dispose() {

        super.dispose(); // dispose all scene objects.
        buttonPressedSound.dispose();
    }

    private final class SetGameHostClickListener extends ClickListener {

        private final boolean isGameHost;

        SetGameHostClickListener(boolean isGameHost) {
            this.isGameHost = isGameHost;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            sceneData.setGameHost(isGameHost);
        }
    }
}
