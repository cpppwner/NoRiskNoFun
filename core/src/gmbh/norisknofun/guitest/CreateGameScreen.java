package gmbh.norisknofun.guitest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Created by Sputzi0815 on 24.04.2017.
 */

public class CreateGameScreen extends Game implements Screen {
    private Stage createGame;
    private Texture img;


    @Override
    public void create(){
        createGame = new Stage();
        img = new Texture("menu.png");

    }

    @Override
    public void render(){
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        createGame.getBatch().begin();
        createGame.getBatch().draw(img,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        createGame.getBatch().end();
        createGame.draw();
    }

    @Override
    public void show() {
        //null
    }

    @Override
    public void render(float delta) {
        //null
    }

    @Override
    public void hide() {
        //null
    }

    @Override
    public void dispose(){
        createGame.dispose();
        img.dispose();
    }

}
