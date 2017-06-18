package gmbh.norisknofun.scene.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import gmbh.norisknofun.assets.AssetModalDialog;
import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.gamemessages.gui.EndGameGui;
import gmbh.norisknofun.scene.Assets;
import gmbh.norisknofun.scene.SceneBase;
import gmbh.norisknofun.scene.SceneData;
import gmbh.norisknofun.scene.SceneNames;
import gmbh.norisknofun.scene.common.BackgroundSceneObject;
import gmbh.norisknofun.scene.common.LabelSceneObject;
import gmbh.norisknofun.scene.common.TextButtonSceneObject;

/**
 * Created by pippp on 18.06.2017.
 */

public class EndGameScene extends SceneBase {
    private final SceneData sceneData;
    private final AssetSound buttonPressedSound;
    private final GameData data;
    private LabelSceneObject sceneObject;


    public EndGameScene(SceneData sceneData){
        super(SceneNames.END_GAME_SCENE, Color.WHITE);
        this.sceneData = sceneData;
        this.data = sceneData.getGameData();
        this.buttonPressedSound = sceneData.createSound(Assets.BUTTON_PRESSED_SOUND_FILENAME);


    }

    @Override
    public void preload(){
        setBackground();
        initLabel();
        initImageButtons();
    }
    private void setBackground() {

        addSceneObject(new BackgroundSceneObject(sceneData.getAssetFactory()));
    }

    private void initLabel() {


        sceneObject = new LabelSceneObject(sceneData.createLabel("", Assets.FONT_110PX_WHITE_WITH_BORDER));
        addSceneObject(sceneObject);
        sceneObject.setBounds(Gdx.graphics.getWidth()/3.0f, Gdx.graphics.getHeight()/1.7f, sceneObject.getWidth(), 200);

    }

    private void initImageButtons() {

        TextButtonSceneObject okButton = new TextButtonSceneObject(sceneData.createTextButton("OK",Assets.DICE_CHEATS_TEXT_BUTTON_DESCRIPTOR), buttonPressedSound);
        okButton.setBounds(Gdx.graphics.getWidth() / 1.6f, Gdx.graphics.getHeight() / 10f, 275f, 240f);
        okButton.addListener(new EndGameScene.ContinueClickListener());
        addSceneObject(okButton);

    }


    @Override
    public void dispose() {

        buttonPressedSound.dispose();
        super.dispose();
    }

    private final class ContinueClickListener extends ClickListener {

        private ContinueClickListener() {
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {


                sceneData.sendMessageFromGui(new EndGameGui());
            Gdx.app.log("EndGameScene","clicked on OK Button");

            super.clicked(event, x, y);
        }

    }

    private void checkErrors() {
        // check for errors and display popup
        String error = data.getLastError();
        if (error != null) {
            AssetModalDialog dialog = sceneData.createModalDialog(error, Assets.ERROR_DIALOG_DESCRIPTOR);
            dialog.show(getStage());
            dialog.setBounds(getStage().getWidth() / 4.0f, getStage().getHeight() / 4.0f,
                    getStage().getWidth() / 2.0f, getStage().getHeight() / 2.0f);
        }
    }

    @Override
    public void show(){
//        if(data.getWinner().equals(data.getMyself().getPlayerName())){
//            sceneObject.setText("You won°!");
//        } else{
//            sceneObject.setText("You lost!");
//        }

        sceneObject.setText("You won!");
        super.show();

    }

    @Override
    public void render(float delta) {
        checkErrors();

        super.render(delta);
    }
}
