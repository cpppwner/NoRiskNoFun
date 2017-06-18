package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.scenes.scene2d.EventListener;

import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.assets.AssetTextButton;
import gmbh.norisknofun.scene.SceneObject;

/**
 * Text button scene object.
 *
 * <p>
 *     In contrary to the {@link ImageButtonSceneObject} this class has text inside.
 *     If the button's text might change, prefer this scene object over the {@link ImageButtonSceneObject}.
 * </p>
 */
public class TextButtonSceneObject extends SceneObject {

    private final AssetTextButton textButton;

    /**
     * Create text button scene object.
     *
     * @param textButton Wrapped text button.
     */
    public TextButtonSceneObject(AssetTextButton textButton) {
        this(textButton, null);
    }

    /**
     * Create text button scene object.
     *
     * @param sound The sound to play if clicked or null to not play any sound.
     */
    public TextButtonSceneObject(AssetTextButton textButton, AssetSound sound) {

        super();
        this.textButton = textButton;

        addListener(new PlaySoundClickListener(sound));
        setSize(textButton.getWidth(), textButton.getHeight());
        addActor(textButton.getActor());
    }

    @Override
    public void setBounds(float x, float y, float width, float height){

        textButton.setBounds(0.0f, 0.0f, width, height);
        super.setBounds(x, y, width, height);
    }

    @Override
    public void dispose() {
        textButton.dispose();
        super.dispose();
    }

    @Override
    public boolean addListener(EventListener listener) {
        return textButton.getActor().addListener(listener);
    }

    @Override
    public boolean removeListener(EventListener listener) {
        return textButton.getActor().removeListener(listener);
    }

    /**
     * Set the text button scene object to disabled.
     *
     * <p>
     *     When set to disabled, the button is not clickable.
     * </p>
     */
    public void setDisabled() {
        textButton.setDisabled();
    }

    /**
     * Set the text button scene object to enabled.
     *
     * <p>
     *     When set to disabled, the button is not clickable.
     * </p>
     */
    public void setEnabled() {
        textButton.setEnabled();
    }

    /**
     * Get a flag indicating whether the button is enabled {@code true} or not {@code false}.
     */
    public boolean isEnabled() {
        return textButton.isEnabled();
    }
}
