package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.EventListener;

import gmbh.norisknofun.assets.AssetLabel;
import gmbh.norisknofun.scene.SceneObject;

/**
 * Special scene object used to draw labels.
 *
 * <p>
 *     Note: The label text can be exchanged later on.
 * </p>
 */
public class LabelSceneObject extends SceneObject {

    /**
     * Label asset, which basically contains the text & font.
     */
    private final AssetLabel label;

    /**
     * Initialize this scene object with given label.
     * @param label The label asset wrapped in this scene object.
     */
    public LabelSceneObject(AssetLabel label) {

        this.label = label;

        setSize(label.getWidth(), label.getHeight());
        addActor(label.getActor());
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {

        label.setBounds(0.0f, 0.0f, width, height);
        super.setBounds(x, y, width, height);
    }

    /**
     * Set the label text.
     * @param text The new text to set in the label.
     */
    public void setText(String text) {

        label.setText(text);
    }

    public void setTextAlignment(int alignment) {

        label.setTextAlignment(alignment);
    }

    /**
     * Set label's background color.
     *
     * @param color Backgroundcolor
     */
    public void setBackgroundColor(Color color) {

        label.setBackgroundColor(color);
    }

    @Override
    public void dispose() {

        label.dispose();
        super.dispose();
    }

    @Override
    public boolean addListener(EventListener listener) {
        return label.getActor().addListener(listener);
    }

    @Override
    public boolean removeListener(EventListener listener) {
        return label.getActor().removeListener(listener);
    }
}
