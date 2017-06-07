package gmbh.norisknofun.scene.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.StartGame;
import gmbh.norisknofun.scene.Assets;
import gmbh.norisknofun.scene.SceneBase;
import gmbh.norisknofun.scene.SceneData;
import gmbh.norisknofun.scene.SceneNames;
import gmbh.norisknofun.scene.common.BackgroundSceneObject;
import gmbh.norisknofun.scene.common.ImageButtonSceneObject;


public class LobbyScene extends SceneBase{

    private final SceneData sceneData;

    /**
     * Sound played when buttons are clicked.
     */
    private final AssetSound buttonPressedSound;

    public LobbyScene(SceneData sceneData) {
        super(SceneNames.LOBBY_SCENE, Color.WHITE);
        this.sceneData = sceneData;
        this.buttonPressedSound = sceneData.createSound(Assets.BUTTON_PRESSED_SOUND_FILENAME);

        setBackground();
        initImageButtons();
    }

    private void setBackground(){

        addSceneObject(new BackgroundSceneObject(sceneData.getAssetFactory()));
    }

    private void initImageButtons() {
        ImageButtonSceneObject imageButtonStartGame = new ImageButtonSceneObject(sceneData.createTexture(Assets.START_GAME_BUTTON_FILENAME), buttonPressedSound);
        imageButtonStartGame.setBounds((Gdx.graphics.getWidth() / 2.0f) - 137.5f, Gdx.graphics.getHeight() / 10.0f, 275f, 240f);
        imageButtonStartGame.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                sceneData.sendMessageFromGui(new StartGame(true));
            }
        });
        addSceneObject(imageButtonStartGame);

    }

    @Override
    public void dispose() {
        buttonPressedSound.dispose();
        super.dispose();
    }
}
