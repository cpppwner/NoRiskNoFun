package gmbh.norisknofun.scene.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.scene.SceneBase;
import gmbh.norisknofun.scene.SceneManager;
import gmbh.norisknofun.scene.SceneNames;

/**
 * Map selection scene.
 */
public final class MapSelectionScene extends SceneBase {

    private BitmapFont font;

    private TextButton buttonMapOne;
    private TextButton buttonMapTwo;

    private final GameData gameData;

    public MapSelectionScene(GameData gameData) {

        super(SceneNames.MAP_SELECTION_SCENE, new Color(-1));

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

        buttonMapOne = createButton("Map One");
        buttonMapTwo = createButton("Map Two");

        buttonMapOne.setBounds(490,500,500,120);
        buttonMapTwo.setBounds(490,250,500,120);

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

        getStage().addActor(buttonMapOne);
        getStage().addActor(buttonMapTwo);
    }

    private TextButton createButton(String buttonText) {

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.up = new TextureRegionDrawable(new TextureRegion(new Texture("button.png")));
        style.down = new TextureRegionDrawable(new TextureRegion(new Texture("button.png")));
        style.fontColor = new Color(0.9f, 0.5f, 0.5f, 1);
        style.downFontColor = new Color(0, 0.4f, 0, 1);

        return new TextButton(buttonText, style);
    }

    @Override
    public void dispose()
    {
        super.dispose();

        font.dispose();
    }
}
