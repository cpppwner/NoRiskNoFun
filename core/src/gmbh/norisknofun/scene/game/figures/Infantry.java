package gmbh.norisknofun.scene.game.figures;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Katharina on 10.04.2017.
 */

public class Infantry extends Figure {

    public Infantry(float x, float y, float width, float height){
        super(x,y,width,height, );
        img= new Texture(Gdx.files.internal("img/infanterie.png"));
        super.sprite.setRegion(img);
    }

    public Infantry(){
        super();
        img= new Texture(Gdx.files.internal("img/infanterie.png"));
        super.sprite.setRegion(img);
    }


}
