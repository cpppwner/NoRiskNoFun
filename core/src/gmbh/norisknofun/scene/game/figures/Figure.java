package gmbh.norisknofun.scene.game.figures;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import gmbh.norisknofun.scene.SceneObject;

/**
 * Created by Katharina on 10.04.2017.
 */

public class Figure extends SceneObject {

    protected Texture img;
    protected Sprite sprite;
    boolean highlighted = false;


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

    public void setHighlighted(Boolean highlighted){
        this.highlighted=highlighted;
        if(highlighted){
            sprite.setRegion(new Texture("badlogic.jpg"));
        }else{
            sprite.setRegion(img);
        }
    }

    public boolean isHighlighted(){
        return highlighted;
    }

    public void addTouchListener() {
        this.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if (isHighlighted()) {
                    setHighlighted(false);
                } else {
                    setHighlighted(true);
                }
                event.stop();
                System.out.println("hallo actor");
                return true;
            }
        });
    }
}
