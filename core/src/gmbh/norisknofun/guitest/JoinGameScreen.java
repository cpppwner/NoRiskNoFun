package gmbh.norisknofun.guitest;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;


/**
 * Created by Sputzi0815 on 24.04.2017.
 */

public class JoinGameScreen extends Game implements Screen{
    private Stage joinGame;
    private Texture img;


    @Override
    public void create() {
        joinGame = new Stage();
        img = new Texture("menu.png");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        joinGame.getBatch().begin();
        joinGame.getBatch().draw(img,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        joinGame.getBatch().end();
        joinGame.draw();
    }

    @Override
    public void show() {
        // null
    }

    @Override
    public void hide() {
        // null
    }

    public void dispose(){
        joinGame.dispose();
        img.dispose();
    }

}
