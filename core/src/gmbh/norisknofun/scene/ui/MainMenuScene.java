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
 * Created by pippp on 06.05.2017.
 */

public class MainMenuScene extends SceneBase{


    private ImageButtonSceneObject imageButtonCreate;
    private ImageButtonSceneObject imageButtonJoin;

    private final GameData gameData;

    public MainMenuScene(GameData gameData){
        super(SceneNames.MAIN_MENU_SCENE, Color.WHITE);
        this.gameData=gameData;
        try {

            setBackground();
            initMenuButtons();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void initMenuButtons() {

        imageButtonCreate = createImageButton("button_create_game_eng.png");
        imageButtonJoin = createImageButton("button_join_game_eng.png");

        imageButtonCreate.setBounds((Gdx.graphics.getWidth()/6)-5,(Gdx.graphics.getHeight()/3),553,480);
        imageButtonJoin.setBounds((Gdx.graphics.getWidth() / 2) + 10, (Gdx.graphics.getHeight() / 3), 553, 480);


        imageButtonCreate.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                SceneManager.getInstance().setActiveScene(SceneNames.CREATE_GAME_SCENE);
            }
        });

        imageButtonJoin.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                SceneManager.getInstance().setActiveScene(SceneNames.JOIN_GAME_SCENE);
            }
        });
        addSceneObject(imageButtonCreate);
        addSceneObject(imageButtonJoin);

    }

    private ImageButtonSceneObject createImageButton (String file){
        Texture txt = new Texture(Gdx.files.internal(file));
        ImageButton imageButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(txt)));
        return new ImageButtonSceneObject(imageButton);
    }


    private void setBackground(){
        addSceneObject(new BackgroundSceneObject());
    }


}
