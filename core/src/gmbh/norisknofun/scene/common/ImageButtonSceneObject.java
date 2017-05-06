package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import gmbh.norisknofun.scene.SceneObject;

/**
 * Created by pippp on 06.05.2017.
 */

public class ImageButtonSceneObject extends SceneObject {

    private ImageButton imageButton;

    public ImageButtonSceneObject(ImageButton imageButton){
        this.imageButton=imageButton;
    }

    public ImageButton getButton(){
        return imageButton;
    }


    @Override
    public void draw(Batch batch, float parentAlpha){
       imageButton.draw(batch,parentAlpha);

    }

    @Override
    public void setBounds(float x, float y, float width, float height){
        super.setBounds(x,y,width,height);
        imageButton.setBounds(x,y,width,height);
    }
}
