package gmbh.norisknofun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFontCache;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;


/**
 * Created by pippp on 01.04.2017.
 */

public class MainMenuScreen implements Screen {

    private final NoRiskNoFun game;
    private OrthographicCamera camera;
    private Stage stage;
    private TextButton button;
    private BitmapFont font;


    public MainMenuScreen(final NoRiskNoFun game){

        this.game = game;
        try {
            float width = Gdx.graphics.getWidth();
            float height = Gdx.graphics.getHeight();
            camera = new OrthographicCamera();
            camera.setToOrtho(false, width, height);
            font = new BitmapFont();
            font.setColor(Color.WHITE);
            font.getData().setScale(3.5f);
            stage = new Stage();
            Gdx.input.setInputProcessor(stage);

            addButton();

            stage.addActor(button);

            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new GameScreen(game));
                }
            });

            stage.act(Gdx.graphics.getDeltaTime()); //Perform ui logic
        }catch (Exception e){
            Gdx.app.error("Error",e.getMessage(),e);
        }

    }

    private void addButton() throws Exception{
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.up = new TextureRegionDrawable(new TextureRegion(new Texture("button.png")));
        style.down = new TextureRegionDrawable(new TextureRegion(new Texture("button.png")));
        style.fontColor = new Color(0.9f, 0.5f, 0.5f, 1);
        style.downFontColor = new Color(0, 0.4f, 0, 1);


        button = new TextButton("Press Me", style);
        button.setBounds(490,120,500,120);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        font.draw(game.batch,"Welcome to DropGame!!", 490,600);
        game.batch.end();
        stage.draw(); //Draw the ui

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        font.dispose();
        stage.dispose();
        game.dispose();

    }


}
