package gmbh.norisknofun.Figures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Katharina on 10.04.2017.
 */

public class Infantry extends Figure {

    public Infantry( int x, int y, int width, int height){
        super(x,y,width,height);
        img= new Texture(Gdx.files.internal("infanterie.png"));
        super.sprite.setRegion(img);
    }

    public Infantry(){
        super();
        img= new Texture(Gdx.files.internal("infanterie.png"));
        super.sprite.setRegion(img);
    }


}
