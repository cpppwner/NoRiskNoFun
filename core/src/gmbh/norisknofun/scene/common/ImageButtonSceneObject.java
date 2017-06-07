package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.assets.AssetTexture;
import gmbh.norisknofun.scene.SceneManager;
import gmbh.norisknofun.scene.SceneNames;
import gmbh.norisknofun.scene.SceneObject;

/**
 * Image button scene object.
 */
public class ImageButtonSceneObject extends ButtonSceneObject {

    private final AssetTexture texture;
    private final AssetSound sound;

    private ImageButton imageButton;

    public ImageButtonSceneObject(AssetTexture texture) {
        this(texture, null);
    }

    public ImageButtonSceneObject(AssetTexture texture, AssetSound sound) {
        super(sound);

        this.texture = texture;
        this.sound = sound;
        this.imageButton = new ImageButton(texture.createDrawable());
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

    @Override
    public void dispose() {
        super.dispose();
        texture.dispose();
    }
}
