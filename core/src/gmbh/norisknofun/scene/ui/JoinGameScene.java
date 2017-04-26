package gmbh.norisknofun.scene.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import gmbh.norisknofun.GUI_Test.NoRiskNoFun_GUI_Test;
import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.scene.SceneBase;
import gmbh.norisknofun.scene.SceneNames;

/**
 * Created by Sputzi0815 on 24.04.2017.
 */

public class JoinGameScene extends SceneBase{
    Stage joinGame;
    Texture img;
    private final GameData data;

    public JoinGameScene(GameData data){
        super(SceneNames.GAME_SCENE, Color.BLACK);
        this.data = data;
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
