package gmbh.norisknofun;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class NoRiskNoFun extends ApplicationAdapter {
	Stage stage;
	Texture img;

	@Override
	public void create () {
		stage = new Stage();
		img = new Texture("menu.png");
	}
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.getBatch().begin();
		stage.getBatch().draw(img,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		stage.getBatch().end();
		stage.draw();

	}
	
	@Override
	public void dispose () {
		stage.dispose();
		img.dispose();
	}



}
