package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import gmbh.norisknofun.scene.SceneObject;

/**
 * Special background scene object
 */
public final class BackgroundSceneObject extends SceneObject {

    private final Texture texture;

    public BackgroundSceneObject(){
        super();
        texture= new Texture("menu.png");

    }

    @Override
    public void draw(Batch batch, float parentAlpha){
       batch.draw(texture,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
