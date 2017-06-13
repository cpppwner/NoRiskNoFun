package gmbh.norisknofun.scene.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

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
import gmbh.norisknofun.scene.common.TextFieldSceneObject;

/**
 * Scene shown, when the user creates a game.
 */
public class CreateGameScene extends SceneBase {

    /**
     * Data class shared amongst the scenes.
     */
    private final SceneData sceneData;

    /**
     * Sound played when buttons are clicked.
     */
    private final AssetSound buttonPressedSound;

    private TextFieldSceneObject textField;

    public CreateGameScene(SceneData sceneData) {

        super(SceneNames.CREATE_GAME_SCENE, Color.WHITE);
        this.sceneData = sceneData;
        this.buttonPressedSound = sceneData.createSound(Assets.BUTTON_PRESSED_SOUND_FILENAME);

        setBackground();
        initNameSelection();
        initImageButtons();
        initLabel();
    }

    private void setBackground() {

        addSceneObject(new BackgroundSceneObject(sceneData.getAssetFactory()));
    }

    private void initNameSelection() {

        // TODO julian - still needs layouting
        LabelSceneObject sceneObject = new LabelSceneObject(sceneData.createLabel(Texts.SELECT_NAME_LABEL, Assets.FONT_60PX_WHITE_WITH_BORDER));
        addSceneObject(sceneObject);
        sceneObject.setBounds(Gdx.graphics.getWidth()/8.0f, Gdx.graphics.getHeight()/2.0f, sceneObject.getWidth(), 125);

        textField = new TextFieldSceneObject(sceneData.createTextField(Assets.NAME_TEXT_FIELD_DESCRIPTOR));
        textField.setBounds((Gdx.graphics.getWidth()/8.0f)+ sceneObject.getWidth(), Gdx.graphics.getHeight()/2.0f, 500,125);
        addSceneObject(textField);
    }

    private void initImageButtons() {

        ImageButtonSceneObject backButton = new ImageButtonSceneObject(sceneData.createImageButton(Assets.BACK_BUTTON_FILENAME), buttonPressedSound);
        ImageButtonSceneObject twoPlayers = new ImageButtonSceneObject(sceneData.createImageButton(Assets.TWO_PLAYERS_BUTTON_FILENAME), buttonPressedSound);
        ImageButtonSceneObject threePlayers = new ImageButtonSceneObject(sceneData.createImageButton(Assets.THREE_PLAYERS_BUTTON_FILENAME), buttonPressedSound);
        ImageButtonSceneObject fourPlayers = new ImageButtonSceneObject(sceneData.createImageButton(Assets.FOUR_PLAYERS_BUTTON_FILENAME), buttonPressedSound);

        backButton.setBounds(Gdx.graphics.getWidth() / 1.5f, Gdx.graphics.getHeight() / 10f, 275f, 240f);
        twoPlayers.setBounds((float) ((Gdx.graphics.getWidth()/10)),(Gdx.graphics.getHeight()/8),275,240);
        threePlayers.setBounds((float) ((Gdx.graphics.getWidth()/10) + 300),(Gdx.graphics.getHeight()/8),275,240);
        fourPlayers.setBounds((float) ((Gdx.graphics.getWidth()/10) + 600),(Gdx.graphics.getHeight()/8),275,240);

        backButton.addListener(new BackClickListener());
        twoPlayers.addListener(new ContinueClickListener(2));
        threePlayers.addListener(new ContinueClickListener(3));
        fourPlayers.addListener(new ContinueClickListener(4));

        addSceneObject(backButton);
        addSceneObject(twoPlayers);
        addSceneObject(threePlayers);
        addSceneObject(fourPlayers);
    }

    /**
     * Initialise the label shown on main menu.
     */
    private void initLabel() {

        LabelSceneObject sceneObject = new LabelSceneObject(sceneData.createLabel(Texts.CREATE_GAME, Assets.FONT_110PX_WHITE_WITH_BORDER));
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

    private final class BackClickListener extends SwitchSceneClickListener {

        private BackClickListener() {
            super(SceneNames.MAIN_MENU_SCENE);
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            sceneData.setMaximumNumberOfPlayers(0);
            super.clicked(event, x, y);
        }
    }

    private final class ContinueClickListener extends SwitchSceneClickListener {

        private final int numPlayersChosen;

        private ContinueClickListener(int numPlayersChosen) {
            super(SceneNames.MAP_SELECTION_SCENE);
            this.numPlayersChosen = numPlayersChosen;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {

            if (textField.getText() == null || textField.getText().isEmpty()) {
                AssetModalDialog dialog = sceneData.createModalDialog("Name is not given", Assets.ERROR_DIALOG_DESCRIPTOR);
                dialog.show(getStage());
                dialog.setBounds(getStage().getWidth() / 4.0f, getStage().getHeight() / 4.0f,
                        getStage().getWidth() / 2.0f, getStage().getHeight() / 2.0f);
            } else {
                sceneData.setPlayerName(textField.getText());
                sceneData.setMaximumNumberOfPlayers(numPlayersChosen);
                super.clicked(event, x, y);
            }
        }
    }
}
