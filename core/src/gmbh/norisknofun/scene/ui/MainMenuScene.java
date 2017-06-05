package gmbh.norisknofun.scene.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import javax.naming.Context;

import gmbh.norisknofun.scene.SceneBase;
import gmbh.norisknofun.scene.SceneManager;
import gmbh.norisknofun.scene.SceneNames;
import gmbh.norisknofun.scene.common.BackgroundSceneObject;
import gmbh.norisknofun.scene.common.ImageButtonSceneObject;

/**
 * Created by pippp on 06.05.2017.
 */

public class MainMenuScene extends SceneBase {

    public MainMenuScene() {
        super(SceneNames.MAIN_MENU_SCENE, Color.WHITE);
        setBackground();
        initMenuButtons();
    }

    private void initMenuButtons() {

        final ImageButtonSceneObject imageButtonCreate;
        ImageButtonSceneObject imageButtonJoin;

        imageButtonCreate = createImageButton("img/button_create_game_eng.png");
        imageButtonJoin = createImageButton("img/button_join_game_eng.png");

        imageButtonCreate.setBounds((Gdx.graphics.getWidth() / 6) - 5, (float) (Gdx.graphics.getHeight() / 2.5), 553, 480);
        imageButtonJoin.setBounds((Gdx.graphics.getWidth() / 2) + 10, (float) (Gdx.graphics.getHeight() / 2.5), 553, 480);

        final Sound sound = Gdx.audio.newSound(Gdx.files.internal("audio/button_pressed.wav"));


        imageButtonCreate.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.play();
                SceneManager.getInstance().setActiveScene(SceneNames.CREATE_GAME_SCENE);
            }
        });

        imageButtonJoin.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                sound.play();
                SceneManager.getInstance().setActiveScene(SceneNames.JOIN_GAME_SCENE);
            }
        });

        addSceneObject(imageButtonCreate);
        addSceneObject(imageButtonJoin);

    }

    private ImageButtonSceneObject createImageButton(String file) {
        Texture txt = new Texture(Gdx.files.internal(file));
        ImageButton imageButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(txt)));
        return new ImageButtonSceneObject(imageButton);
    }

    private void setBackground() {
        addSceneObject(new BackgroundSceneObject());
    }



}
