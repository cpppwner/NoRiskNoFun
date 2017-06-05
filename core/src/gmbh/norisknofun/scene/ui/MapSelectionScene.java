package gmbh.norisknofun.scene.ui;

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

/**
 * Map selection scene.
 */
public final class MapSelectionScene extends SceneBase {

    private BitmapFont font;

    private final GameData gameData;

    public MapSelectionScene(GameData gameData) {

        super(SceneNames.MAP_SELECTION_SCENE, Color.WHITE);
        setBackground();
        initFont();
        initMapSelectionButtons();



        this.gameData = gameData;
    }

    private void initFont() {

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(3.5f);
    }

    private void initMapSelectionButtons() {
        TextButtonSceneObject buttonMapOne;
        TextButtonSceneObject buttonMapTwo;
        ImageButtonSceneObject imageButtonBack;

        buttonMapOne = createButton("Map One");
        buttonMapTwo = createButton("Map Two");
        imageButtonBack = createImageButton("button_back.png");


        buttonMapOne.setBounds(490,500,500,120);
        buttonMapTwo.setBounds(490,250,500,120);
        imageButtonBack.setBounds((float) (Gdx.graphics.getWidth()/1.5),(Gdx.graphics.getHeight()/10),275,240);


        buttonMapOne.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameData.setMapFile("maps/Dummy One.map");
                SceneManager.getInstance().setActiveScene(SceneNames.GAME_SCENE);
            }
        });

        buttonMapTwo.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                gameData.setMapFile("maps/Dummy Two.map");
                SceneManager.getInstance().setActiveScene(SceneNames.GAME_SCENE);
            }
        });

        imageButtonBack.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                SceneManager.getInstance().setActiveScene(SceneNames.CREATE_GAME_SCENE);
            }
        });

        addSceneObject(buttonMapOne);
        addSceneObject(buttonMapTwo);
        addSceneObject(imageButtonBack);
    }

    private TextButtonSceneObject createButton(String buttonText) {

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.up = new TextureRegionDrawable(new TextureRegion(new Texture("button.png")));
        style.down = new TextureRegionDrawable(new TextureRegion(new Texture("button.png")));
        style.fontColor = new Color(0f, 0f, 0f, 1);
        style.downFontColor = new Color(0, 0.4f, 0, 1);

        return new TextButtonSceneObject(new TextButton(buttonText, style));
    }


    private ImageButtonSceneObject createImageButton (String file){
        Texture txt = new Texture(Gdx.files.internal(file));
        ImageButton imageButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(txt)));
        return new ImageButtonSceneObject(imageButton);
    }

    private void setBackground() {
        addSceneObject(new BackgroundSceneObject());
    }

    @Override
    public void dispose()
    {
        super.dispose();
        font.dispose();
    }
}
