package gmbh.norisknofun.scene.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import gmbh.norisknofun.assets.AssetModalDialog;
import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.gamemessages.gui.ChooseTroopsAmountGui;
import gmbh.norisknofun.scene.Assets;
import gmbh.norisknofun.scene.SceneBase;
import gmbh.norisknofun.scene.SceneData;
import gmbh.norisknofun.scene.SceneNames;
import gmbh.norisknofun.scene.Texts;
import gmbh.norisknofun.scene.common.BackgroundSceneObject;
import gmbh.norisknofun.scene.common.ImageButtonSceneObject;
import gmbh.norisknofun.scene.common.LabelSceneObject;
import gmbh.norisknofun.scene.common.NumericFieldSceneObject;
import gmbh.norisknofun.scene.common.SwitchSceneClickListener;
import gmbh.norisknofun.scene.common.TextButtonSceneObject;
import gmbh.norisknofun.scene.common.TextFieldSceneObject;
import gmbh.norisknofun.scene.ui.CreateGameScene;

/**
 * Created by pippp on 17.06.2017.
 */

public class ChooseTroopAmountScene extends SceneBase {


    private final SceneData sceneData;
    private final AssetSound buttonPressedSound;
    private final GameData data;

    private NumericFieldSceneObject numField;

    public ChooseTroopAmountScene(SceneData sceneData){
        super(SceneNames.TROOP_AMOUNT_SCENE, Color.WHITE);
        this.sceneData = sceneData;
        this.data = sceneData.getGameData();
        this.buttonPressedSound = sceneData.createSound(Assets.BUTTON_PRESSED_SOUND_FILENAME);


    }

    @Override
    public void preload(){
        setBackground();
        initNumField();
        initImageButtons();
    }
    private void setBackground() {

        addSceneObject(new BackgroundSceneObject(sceneData.getAssetFactory()));
    }

    private void initNumField() {

        LabelSceneObject sceneObject = new LabelSceneObject(sceneData.createLabel(Texts.CHOOSE_TROOP_AMOUNT, Assets.FONT_60PX_WHITE_WITH_BORDER));
        addSceneObject(sceneObject);
        sceneObject.setBounds(Gdx.graphics.getWidth()/2.0f-(sceneObject.getWidth()/2), Gdx.graphics.getHeight()/1.2f, sceneObject.getWidth(), 125);

        numField = new NumericFieldSceneObject(sceneData.createNumericField(Assets.NAME_TEXT_FIELD_DESCRIPTOR));
        numField.setBounds((Gdx.graphics.getWidth()/8.0f), Gdx.graphics.getHeight()/2.0f, 500,125);
        addSceneObject(numField);
    }

    private void initImageButtons() {

        TextButtonSceneObject okButton = new TextButtonSceneObject(sceneData.createTextButton("OK",Assets.DICE_CHEATS_TEXT_BUTTON_DESCRIPTOR), buttonPressedSound);
        okButton.setBounds(Gdx.graphics.getWidth() / 1.5f, Gdx.graphics.getHeight() / 10f, 275f, 240f);
        okButton.addListener(new ChooseTroopAmountScene.ContinueClickListener());
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

           if (numField.getValue()==0 ) {
                showDialog("Number not given");
            } else {
                sceneData.sendMessageFromGui(new ChooseTroopsAmountGui(numField.getValue()));
            }
            super.clicked(event, x, y);
        }

        private void showDialog(String message){
            AssetModalDialog dialog = sceneData.createModalDialog(message, Assets.ERROR_DIALOG_DESCRIPTOR);
            dialog.show(getStage());
            dialog.setBounds(getStage().getWidth() / 4.0f, getStage().getHeight() / 4.0f,
                    getStage().getWidth() / 2.0f, getStage().getHeight() / 2.0f);
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
    public void render(float delta) {
        checkErrors();

        super.render(delta);
    }

}
