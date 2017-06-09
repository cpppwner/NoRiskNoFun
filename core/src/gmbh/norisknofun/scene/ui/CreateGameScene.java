package gmbh.norisknofun.scene.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
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

        LabelSceneObject sceneObject = new LabelSceneObject(sceneData.createLabel(Texts.SELECT_NAME_LABEL, Assets.FONT_60PX_WHITE_WITH_BORDER));
        addSceneObject(sceneObject);
        sceneObject.setBounds((Gdx.graphics.getWidth() - sceneObject.getWidth()) / 2.0f,
                sceneObject.getHeight() * 4.0f,
                sceneObject.getWidth(),
                sceneObject.getHeight());

        TextFieldSceneObject textField = new TextFieldSceneObject(sceneData.createTextField(Assets.NAME_TEXT_FIELD_DESCRIPTOR));
        textField.setBounds(500, 500, textField.getWidth(), textField.getHeight());
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

        ClickListener switchToLobbySceneListener = new SwitchSceneClickListener(SceneNames.MAP_SELECTION_SCENE);
        backButton.addListener(new SetNumPlayersClickListener(0));
        backButton.addListener(new SwitchSceneClickListener(SceneNames.MAIN_MENU_SCENE));
        twoPlayers.addListener(new SetNumPlayersClickListener(2));
        twoPlayers.addListener(switchToLobbySceneListener);
        threePlayers.addListener(new SetNumPlayersClickListener(3));
        threePlayers.addListener(switchToLobbySceneListener);
        fourPlayers.addListener(new SetNumPlayersClickListener(4));
        fourPlayers.addListener(switchToLobbySceneListener);

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
        sceneObject.setPosition((Gdx.graphics.getWidth() - sceneObject.getWidth()) / 2.0f,
                sceneObject.getHeight() * 3.0f);
    }

    @Override
    public void dispose() {

        buttonPressedSound.dispose();
        super.dispose();
    }

    private final class SetNumPlayersClickListener extends ClickListener {

        private final int numPlayers;

        SetNumPlayersClickListener(int numPlayers) {
            this.numPlayers = numPlayers;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {

            sceneData.setMaximumNumberOfPlayers(numPlayers);
        }
    }
}
