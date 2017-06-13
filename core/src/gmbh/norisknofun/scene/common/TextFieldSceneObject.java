package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.scenes.scene2d.EventListener;

import gmbh.norisknofun.assets.AssetTextField;
import gmbh.norisknofun.scene.SceneObject;

/**
 * Scene object for text fields.
 */
public class TextFieldSceneObject extends SceneObject {

    private final AssetTextField textField;

    public TextFieldSceneObject(AssetTextField textField) {

        this.textField = textField;

        setSize(textField.getWidth(), textField.getHeight());
        addActor(textField.getActor());
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {

        textField.setBounds(0.0f, 0.0f, width, height);
        super.setBounds(x, y, width, height);
    }

    public String getText() {
        return textField.getText();
    }

    @Override
    public void dispose() {
        textField.dispose();
        super.dispose();
    }

    @Override
    public boolean addListener(EventListener listener) {
        return textField.getActor().addListener(listener);
    }

    @Override
    public boolean removeListener(EventListener listener) {
        return textField.getActor().removeListener(listener);
    }
}
