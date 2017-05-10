package gmbh.norisknofun.Figures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Katharina on 10.04.2017.
 */

public class Cavalry extends Figure {

    public Cavalry( int x, int y, int width, int height){
        super(x,y,width,height);
        img= new Texture(Gdx.files.internal("kavalerie.png"));
        super.sprite.setRegion(img);
    }

    public Cavalry(){
        super();
        img= new Texture(Gdx.files.internal("kavalerie.png"));
        super.sprite.setRegion(img);
    }

}
