package gmbh.norisknofun.scene.ui;

/**
 * Created by Sputzi0815 on 17.05.2017.
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.scene.SceneBase;
import gmbh.norisknofun.scene.SceneManager;
import gmbh.norisknofun.scene.SceneNames;
import gmbh.norisknofun.scene.common.BackgroundSceneObject;
import gmbh.norisknofun.scene.common.ImageButtonSceneObject;
import gmbh.norisknofun.scene.common.TextButtonSceneObject;


public class LobbyScene extends SceneBase{

    public LobbyScene() {
        super(SceneNames.LOBBY_SCENE, Color.WHITE);
        setBackground();
        initImageButtons();
    }

    private void setBackground(){
        addSceneObject(new BackgroundSceneObject());
    }

    private void initImageButtons() {
        ImageButtonSceneObject /*imageButtonBack,*/ imageButtonStartGame;
/*        imageButtonBack = createImageButton("button_back.png");
        imageButtonBack.setBounds((float) (Gdx.graphics.getWidth()/1.5),(Gdx.graphics.getHeight()/10),275,240);
        imageButtonBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                SceneManager.getInstance().setActiveScene(SceneNames.MAIN_MENU_SCENE);
            }
        });
        addSceneObject(imageButtonBack);
*/
        imageButtonStartGame = createImageButton("img/start_game_button.png");
        imageButtonStartGame.setBounds((float) ((Gdx.graphics.getWidth()/2)-137.5),(Gdx.graphics.getHeight()/10),275,240);
        imageButtonStartGame.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                SceneManager.getInstance().setActiveScene(SceneNames.MAP_SELECTION_SCENE);
            }
        });
        addSceneObject(imageButtonStartGame);

    }

    private ImageButtonSceneObject createImageButton (String file){
        Texture txt = new Texture(Gdx.files.internal(file));
        ImageButton imageButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(txt)));
        return new ImageButtonSceneObject(imageButton);
    }

}
