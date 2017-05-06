package gmbh.norisknofun.scene.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.scene.SceneBase;
import gmbh.norisknofun.scene.SceneManager;
import gmbh.norisknofun.scene.SceneNames;
import gmbh.norisknofun.scene.common.BackgroundSceneObject;
import gmbh.norisknofun.scene.common.ImageButtonSceneObject;

/**
 * Created by Sputzi0815 on 24.04.2017.
 */

public class CreateGameScene extends SceneBase {

    private ImageButtonSceneObject createGameButton;
    private final GameData data;

    public CreateGameScene(GameData data) {

        super(SceneNames.CREATE_GAME_SCENE, Color.WHITE);
        this.data = data;
        setBackground();
        initImageButtons();
    }


    @Override
    public void dispose(){
       super.dispose();
    }

    private void setBackground(){
        addSceneObject(new BackgroundSceneObject());
    }

    private void initImageButtons() {

        createGameButton = createImageButton("button_create_game_eng.png");
        createGameButton.setBounds((Gdx.graphics.getWidth()/6)-5,(Gdx.graphics.getHeight()/3),553,480);


        createGameButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                SceneManager.getInstance().setActiveScene(SceneNames.MAP_SELECTION_SCENE);
            }
        });

        addSceneObject(createGameButton);

    }

    private ImageButtonSceneObject createImageButton (String file){
        Texture txt = new Texture(Gdx.files.internal(file));
        ImageButton imageButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(txt)));
        return new ImageButtonSceneObject(imageButton);
    }
}
