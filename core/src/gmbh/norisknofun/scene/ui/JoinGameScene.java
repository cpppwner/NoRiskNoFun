package gmbh.norisknofun.scene.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.scene.Assets;
import gmbh.norisknofun.scene.SceneBase;
import gmbh.norisknofun.scene.SceneData;
import gmbh.norisknofun.scene.SceneManager;
import gmbh.norisknofun.scene.SceneNames;
import gmbh.norisknofun.scene.common.BackgroundSceneObject;
import gmbh.norisknofun.scene.common.ImageButtonSceneObject;
import gmbh.norisknofun.scene.common.SwitchSceneClickListener;

/**
 * Scene shown when user wants to join a server.
 */
public class JoinGameScene extends SceneBase {

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
     */
    public JoinGameScene(SceneData sceneData) {
        super(SceneNames.JOIN_GAME_SCENE, Color.WHITE);
        this.sceneData = sceneData;
        this.buttonPressedSound = sceneData.createSound(Assets.BUTTON_PRESSED_SOUND_FILENAME);

        setBackground();
        initImageButtons();
    }

    private void  setBackground(){
        addSceneObject(new BackgroundSceneObject(sceneData.getAssetFactory()));
    }

    private void initImageButtons() {

        ImageButtonSceneObject imageButtonBack = new ImageButtonSceneObject(sceneData.createTexture(Assets.BACK_BUTTON_FILENAME), buttonPressedSound);
        ImageButtonSceneObject joinGameButton = new ImageButtonSceneObject(sceneData.createTexture(Assets.JOIN_GAME_BUTTON_FILENAME), buttonPressedSound);

        joinGameButton.setBounds((float) ((Gdx.graphics.getWidth()/2)-137.5),(Gdx.graphics.getHeight()/10),275,240);
        imageButtonBack.setBounds((float) (Gdx.graphics.getWidth()/1.5),(Gdx.graphics.getHeight()/10),275,240);

        joinGameButton.addListener(new SwitchSceneClickListener(SceneNames.LOBBY_SCENE));
        imageButtonBack.addListener(new SwitchSceneClickListener(SceneNames.MAIN_MENU_SCENE));

        addSceneObject(joinGameButton);
        addSceneObject(imageButtonBack);
    }

    @Override
    public void dispose() {
        buttonPressedSound.dispose();
        super.dispose();
    }
}
