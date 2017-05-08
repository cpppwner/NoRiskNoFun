package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import gmbh.norisknofun.scene.SceneObject;

/**
 * Created by user on 08.05.17.
 */

public class LabelSceneObject extends SceneObject{

    private Label label;

    public LabelSceneObject(Label label) {
        this.label = label;
    }

    public Label getLabel() {
        return label;
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
}
