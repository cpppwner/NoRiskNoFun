package gmbh.norisknofun.scene.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

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
import gmbh.norisknofun.scene.common.TextButtonSceneObject;

/**
 * Map selection scene.
 */
public final class MapSelectionScene extends SceneBase {

    private static final String MAP_ONE_BUTTON_TEXT = "Map One";
    private static final String MAP_TWO_BUTTON_TEXT = "Map Two";

    private final SceneData sceneData;
    private final AssetSound buttonPressedSound;

    public MapSelectionScene(SceneData sceneData) {

        super(SceneNames.MAP_SELECTION_SCENE, Color.WHITE);
        this.sceneData = sceneData;
        this.buttonPressedSound = sceneData.createSound(Assets.BUTTON_PRESSED_SOUND_FILENAME);

        setBackground();
        initMapSelectionButtons();
        initLabel();
    }

    private void setBackground() {
        addSceneObject(new BackgroundSceneObject(sceneData.getAssetFactory()));
    }

    private void initMapSelectionButtons() {
        TextButtonSceneObject buttonMapOne = new TextButtonSceneObject(sceneData.getAssetFactory(), MAP_ONE_BUTTON_TEXT, buttonPressedSound);
        TextButtonSceneObject buttonMapTwo = new TextButtonSceneObject(sceneData.getAssetFactory(), MAP_TWO_BUTTON_TEXT, buttonPressedSound);
        ImageButtonSceneObject backButton = new ImageButtonSceneObject(sceneData.createTexture(Assets.BACK_BUTTON_FILENAME), buttonPressedSound);

        buttonMapOne.setBounds(490,500,500,120);
        buttonMapTwo.setBounds(490,250,500,120);
        backButton.setBounds(Gdx.graphics.getWidth() / 1.5f, Gdx.graphics.getHeight() / 10.0f, 275f, 240f);

        EventListener switchToLobbySceneListener = new SwitchSceneClickListener(SceneNames.LOBBY_SCENE);
        buttonMapOne.addListener(new SetSelectedMapClickListener("maps/Dummy One.map"));
        buttonMapOne.addListener(switchToLobbySceneListener);
        buttonMapTwo.addListener(new SetSelectedMapClickListener("maps/Dummy Two.map"));
        buttonMapTwo.addListener(switchToLobbySceneListener);
        backButton.addListener(new SetSelectedMapClickListener(null));
        backButton.addListener(new SwitchSceneClickListener(SceneNames.CREATE_GAME_SCENE));

        addSceneObject(buttonMapOne);
        addSceneObject(buttonMapTwo);
        addSceneObject(backButton);
    }

    /**
     * Initialise the label shown on main menu.
     */
    private void initLabel() {

        LabelSceneObject sceneObject = new LabelSceneObject(sceneData.createLabel(Texts.MAP_SELECTION,
                sceneData.createFont(110, Color.WHITE, 2.0f)));
        addSceneObject(sceneObject);
        sceneObject.setBounds((Gdx.graphics.getWidth() - sceneObject.getWidth()) / 2.0f,
                sceneObject.getHeight() * 3.0f,
                sceneObject.getWidth(),
                Gdx.graphics.getHeight() - sceneObject.getHeight());
    }

    @Override
    public void dispose() {
        buttonPressedSound.dispose();
        super.dispose();
    }

    private final class SetSelectedMapClickListener extends ClickListener {

        private final String mapFilename;

        SetSelectedMapClickListener(String mapFilename) {

            this.mapFilename = mapFilename;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            sceneData.setMapFilename(mapFilename);
        }
    }
}
