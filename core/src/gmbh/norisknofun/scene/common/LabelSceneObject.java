package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import gmbh.norisknofun.assets.AssetLabel;
import gmbh.norisknofun.scene.SceneObject;

/**
 * Created by user on 08.05.17.
 */

public class LabelSceneObject extends SceneObject{

    private final AssetLabel label;

    public LabelSceneObject(AssetLabel label) {
        this.label = label;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        label.draw(batch, parentAlpha);
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y, width, height);
        label.setBounds(x, y, width, height);
    }

    public void setText(String text) {

        label.setText(text);
    }
}
