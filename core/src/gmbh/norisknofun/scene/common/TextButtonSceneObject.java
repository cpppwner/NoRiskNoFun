package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import gmbh.norisknofun.scene.SceneObject;

/**
 * Created by pippp on 06.05.2017.
 */

public class TextButtonSceneObject extends SceneObject {

    private TextButton textButton;

    public TextButtonSceneObject(TextButton textButton){
        this.textButton=textButton;
    }

    public TextButton getButton(){
        return textButton;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        textButton.draw(batch,parentAlpha);

    }

    @Override
    public void setBounds(float x, float y, float width, float height){
        super.setBounds(x,y,width,height);
        textButton.setBounds(x,y,width,height);
    }
}
