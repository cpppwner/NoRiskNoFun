package gmbh.norisknofun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by pippp on 05.04.2017.
 */

public  class MyActor extends Actor {
    private Texture img;
    private Sprite sprite;

    public MyActor(){
        sprite = new Sprite();
        img = new Texture(Gdx.files.internal("waterdrop.png"));
        sprite.setRegion(img);
//            setTouchable(Touchable.enabled);
//            addListener(new InputListener(){
//                @Override
//                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
//                    System.out.println("down");
//                    return true;
//                }
//            });

    }
    @Override
    public void draw(Batch batch, float parentAlpha){
        batch.draw(sprite, getX(),getY(),getWidth(),getHeight());

    }

    @Override
    protected void positionChanged() {
        sprite.setPosition(getX(),getY());
        super.positionChanged();
    }

}