package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.scenes.scene2d.EventListener;

import gmbh.norisknofun.assets.AssetNumericField;
import gmbh.norisknofun.scene.SceneObject;

/**
 * Scene object for numeric input fields.
 */
public class NumericFieldSceneObject extends SceneObject {

    /**
     * Asset behind the scene object.
     */
    private final AssetNumericField numericField;

    /**
     * Constructor taking the asset.
     */
    public NumericFieldSceneObject(AssetNumericField numericField) {

        this.numericField = numericField;

        setSize(numericField.getWidth(), numericField.getHeight());
        addActor(numericField.getActor());
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {

        numericField.setBounds(0.0f, 0.0f, width, height);
        super.setBounds(x, y, width, height);
    }

    /**
     * Get the numeric value.
     */
    public int getValue() {
        return numericField.getValue();
    }

    @Override
    public void dispose() {
        numericField.dispose();
        super.dispose();
    }

    @Override
    public boolean addListener(EventListener listener) {
        return numericField.getActor().addListener(listener);
    }

    @Override
    public boolean removeListener(EventListener listener) {
        return numericField.getActor().removeListener(listener);
    }
}
