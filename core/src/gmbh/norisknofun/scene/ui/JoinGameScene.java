package gmbh.norisknofun.scene.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

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
     * Text field where user can enter the name he uses in the game.
     */
    private TextFieldSceneObject nameTextField;

    /**
     * Text field where user can enter the server's ip or hostname, which is hosting the game.
     */
    private TextFieldSceneObject hostTextField;


    /**
     * Construct and initialize main menu scene.
     *
     * @param sceneData Contained class shared amongst scenes.
     */
    public JoinGameScene(SceneData sceneData) {
        super(SceneNames.JOIN_GAME_SCENE, Color.WHITE);
        this.sceneData = sceneData;
        this.buttonPressedSound = sceneData.createSound(Assets.BUTTON_PRESSED_SOUND_FILENAME);

        setBackground();
        initImageButtons();
        initNameSelection();
        initHostSelection();
        initLabel();
    }

    private void setBackground() {
        addSceneObject(new BackgroundSceneObject(sceneData.getAssetFactory()));
    }

    private void initImageButtons() {

        ImageButtonSceneObject imageButtonBack = new ImageButtonSceneObject(sceneData.createImageButton(Assets.BACK_BUTTON_FILENAME), buttonPressedSound);
        ImageButtonSceneObject joinGameButton = new ImageButtonSceneObject(sceneData.createImageButton(Assets.JOIN_GAME_BUTTON_FILENAME), buttonPressedSound);

        joinGameButton.setBounds((float) ((Gdx.graphics.getWidth() / 2) - 137.5), (Gdx.graphics.getHeight() / 10), 275, 240);
        imageButtonBack.setBounds((float) (Gdx.graphics.getWidth() / 1.5), (Gdx.graphics.getHeight() / 10), 275, 240);

        joinGameButton.addListener(new SwitchToLobbySceneListener());
        imageButtonBack.addListener(new SwitchSceneClickListener(SceneNames.MAIN_MENU_SCENE));

        addSceneObject(joinGameButton);
        addSceneObject(imageButtonBack);
    }

    private void initNameSelection() {

        LabelSceneObject sceneObject = new LabelSceneObject(sceneData.createLabel(Texts.ENTER_NAME_LABEL, Assets.FONT_60PX_WHITE_WITH_BORDER));
        addSceneObject(sceneObject);
        sceneObject.setBounds(Gdx.graphics.getWidth() / 8.0f, Gdx.graphics.getHeight() / 1.8f, sceneObject.getWidth(), 125);

        nameTextField = new TextFieldSceneObject(sceneData.createTextField(Assets.NAME_TEXT_FIELD_DESCRIPTOR));
        nameTextField.setBounds((Gdx.graphics.getWidth() / 8.0f) + sceneObject.getWidth(), Gdx.graphics.getHeight() / 1.8f, 500, 125);
        addSceneObject(nameTextField);
    }

    private void initHostSelection() {

        LabelSceneObject sceneObject = new LabelSceneObject(sceneData.createLabel(Texts.ENTER_SERVER_IP_LABEL, Assets.FONT_60PX_WHITE_WITH_BORDER));
        addSceneObject(sceneObject);
        sceneObject.setBounds(Gdx.graphics.getWidth() / 8.0f, Gdx.graphics.getHeight() / 2.8f, sceneObject.getWidth(), 125);

        hostTextField = new TextFieldSceneObject(sceneData.createTextField(Assets.IP_ADDRESS_TEXT_FIELD_DESCRIPTOR));
        hostTextField.setBounds((Gdx.graphics.getWidth() / 8.0f) + sceneObject.getWidth(), Gdx.graphics.getHeight() / 2.8f, 500, 125);
        addSceneObject(hostTextField);
    }

    /**
     * Initialise the label shown on Join Game Scene.
     */
    private void initLabel() {

        LabelSceneObject sceneObject = new LabelSceneObject(sceneData.createLabel(Texts.JOIN_GAME, Assets.FONT_110PX_WHITE_WITH_BORDER));
        addSceneObject(sceneObject);
        sceneObject.setBounds((Gdx.graphics.getWidth() - sceneObject.getWidth()) / 2.0f,
                Gdx.graphics.getHeight() - (sceneObject.getHeight() * 2.0f),
                sceneObject.getWidth(),
                sceneObject.getHeight());
    }

    @Override
    public void dispose() {
        buttonPressedSound.dispose();
        super.dispose();
    }

    private final class SwitchToLobbySceneListener extends SwitchSceneClickListener {

        SwitchToLobbySceneListener() {
            super(SceneNames.LOBBY_SCENE);
        }


        @Override
        public void clicked(InputEvent event, float x, float y) {
//            if (nameTextField.getText() == null || nameTextField.getText().isEmpty()) {
//                AssetModalDialog dialog = sceneData.createModalDialog("Name is not given", Assets.ERROR_DIALOG_DESCRIPTOR);
//                dialog.show(getStage());
//                dialog.setBounds(getStage().getWidth() / 4.0f, getStage().getHeight() / 4.0f,
//                        getStage().getWidth() / 2.0f, getStage().getHeight() / 2.0f);
//            } else if (hostTextField.getText() == null || hostTextField.getText().isEmpty()) {
//                AssetModalDialog dialog = sceneData.createModalDialog("IP is not given", Assets.ERROR_DIALOG_DESCRIPTOR);
//                dialog.show(getStage());
//                dialog.setBounds(getStage().getWidth() / 4.0f, getStage().getHeight() / 4.0f,
//                        getStage().getWidth() / 2.0f, getStage().getHeight() / 2.0f);
//            }
//            else{
//
//                sceneData.setHostIp(hostTextField.getText());
//                sceneData.setPlayerName(nameTextField.getText());
//                super.clicked(event, x, y);
//            }

                sceneData.setHostIp("192.168.43.1");
            if ( !nameTextField.getText().isEmpty()) {
                sceneData.setPlayerName(nameTextField.getText());
            } else {
                sceneData.setPlayerName("PlayerJoining");
            }
            super.clicked(event, x, y);
        }
    }
}
