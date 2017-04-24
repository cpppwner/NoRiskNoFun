package gmbh.norisknofun;

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

public class JoinGameScreen extends Game implements Screen{
    Stage joinGame;
    Texture img;
    Texture imgbtn;
    TextureRegion imgbtnregion;
    TextureRegionDrawable textureRegionDrawable;
    ImageButton imageButton;

    public JoinGameScreen(NoRiskNoFun game){

    }

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

    }

    @Override
    public void hide() {

    }

    public void dispose(){
        joinGame.dispose();
        img.dispose();
    }

}
