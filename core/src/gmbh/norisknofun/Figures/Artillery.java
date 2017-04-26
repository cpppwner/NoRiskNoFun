package gmbh.norisknofun.Figures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Katharina on 10.04.2017.
 */

public class Artillery extends Figure {



    public Artillery( int x, int y, int width, int height){
        super(x,y,width,height);
        img= new Texture(Gdx.files.internal("artillerie.png"));
       super.sprite.setRegion(img);
    }

    public Artillery(){
        img= new Texture(Gdx.files.internal("artillerie.png"));
        super.sprite.setRegion(img);
    }
}