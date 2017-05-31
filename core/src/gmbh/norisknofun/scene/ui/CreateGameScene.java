package gmbh.norisknofun.scene.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import gmbh.norisknofun.scene.SceneBase;
import gmbh.norisknofun.scene.SceneManager;
import gmbh.norisknofun.scene.SceneNames;
import gmbh.norisknofun.scene.common.BackgroundSceneObject;
import gmbh.norisknofun.scene.common.ImageButtonSceneObject;

/**
 * Created by Sputzi0815 on 24.04.2017.
 */

public class CreateGameScene extends SceneBase {

    public CreateGameScene() {

        super(SceneNames.CREATE_GAME_SCENE, Color.WHITE);
        setBackground();
        initImageButtons();
    }



    private void setBackground(){
        addSceneObject(new BackgroundSceneObject());
    }

    private void initImageButtons() {
        ImageButtonSceneObject createGameButton, imageButtonBack, twoPlayers, threePlayers, fourPlayers;

        createGameButton = createImageButton("button_create_game_eng.png");
        imageButtonBack = createImageButton("button_back.png");
        twoPlayers = createImageButton("button_2_players.png");
        threePlayers = createImageButton("button_3_players.png");
        fourPlayers = createImageButton("button_4_players.png");


        createGameButton.setBounds((Gdx.graphics.getWidth()/6)-5, (float) (Gdx.graphics.getHeight()/2.5),553,480);
        imageButtonBack.setBounds((float) (Gdx.graphics.getWidth()/1.5),(Gdx.graphics.getHeight()/10),275,240);
        twoPlayers.setBounds((float) ((Gdx.graphics.getWidth()/10)),(Gdx.graphics.getHeight()/8),275,240);
        threePlayers.setBounds((float) ((Gdx.graphics.getWidth()/10) + 300),(Gdx.graphics.getHeight()/8),275,240);
        fourPlayers.setBounds((float) ((Gdx.graphics.getWidth()/10) + 600),(Gdx.graphics.getHeight()/8),275,240);


        createGameButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                SceneManager.getInstance().setActiveScene(SceneNames.MAP_SELECTION_SCENE);
            }
        });

        imageButtonBack.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                SceneManager.getInstance().setActiveScene(SceneNames.MAIN_MENU_SCENE);
            }
        });

        twoPlayers.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                SceneManager.getInstance().setActiveScene(SceneNames.MAP_SELECTION_SCENE);
            }
        });

        threePlayers.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                SceneManager.getInstance().setActiveScene(SceneNames.MAP_SELECTION_SCENE);
            }
        });

        fourPlayers.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                SceneManager.getInstance().setActiveScene(SceneNames.MAP_SELECTION_SCENE);
            }
        });




        addSceneObject(createGameButton);
        addSceneObject(imageButtonBack);
        addSceneObject(twoPlayers);
        addSceneObject(threePlayers);
        addSceneObject(fourPlayers);
    }

    private ImageButtonSceneObject createImageButton (String file){
        Texture txt = new Texture(Gdx.files.internal(file));
        ImageButton imageButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(txt)));
        return new ImageButtonSceneObject(imageButton);
    }
}
