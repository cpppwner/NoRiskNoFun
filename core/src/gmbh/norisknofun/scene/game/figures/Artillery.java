package gmbh.norisknofun.scene.game.figures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Katharina on 10.04.2017.
 */

public class Artillery extends Figure {



    public Artillery(float x, float y, float width, float height, int id){
        super(x,y,width,height, id);
        img= new Texture(Gdx.files.internal("img/artillerie.png"));
       super.sprite.setRegion(img);
    }

    public Artillery(){
        img= new Texture(Gdx.files.internal("img/artillerie.png"));
        super.sprite.setRegion(img);
    }
}
