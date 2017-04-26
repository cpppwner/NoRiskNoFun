package gmbh.norisknofun.Figures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Katharina on 10.04.2017.
 */

public class Figure extends Actor {

    protected Texture img;
    protected Sprite sprite;



    public Figure( int x, int y, int width, int height){


        sprite = new Sprite();
        setBounds(x,y,width,height);
    }

    public Figure(){

        sprite= new Sprite();
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
