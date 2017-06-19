package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import gmbh.norisknofun.assets.AssetImageButton;
import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.scene.SceneObject;

/**
 * Image button scene object.
 *
 * <p>
 *     An image button is an image acting like a button (pressable and such).
 *     This objects basically wraps the underlying libgdx {@link ImageButton}.
 * </p>
 */
public class ImageButtonSceneObject extends SceneObject {

    /**
     * Image button asset.
     */
    private final AssetImageButton imageButton;

    /**
     * Initialize image button scene object with given image button asset
     *
     * @param imageButton The image button asset.
     *                The image button is disposed by this scene object.
     */
    public ImageButtonSceneObject(AssetImageButton imageButton) {
        this(imageButton, null);
    }

    /**
     * Initialize image button with texture and sound to play on button click.
     *
     * @param imageButton The image button asset.
     *                The image button is disposed by this scene object.
     * @param sound Sound played when the button is pressed.
     *              Note: The caller of of this constructor is responsible for disposing the sound,
     *              but only when the button instance is no longer used.
     */
    public ImageButtonSceneObject(AssetImageButton imageButton, AssetSound sound) {

        super();
        this.imageButton = imageButton;

        addListener(new PlaySoundClickListener(sound));
        setSize(imageButton.getWidth(), imageButton.getHeight());
        addActor(imageButton.getActor());
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {

        imageButton.setBounds(0.0f, 0.0f, width, height);
        super.setBounds(x, y, width, height);
    }

    @Override
    public void dispose() {
        imageButton.dispose();
        super.dispose();
    }

    @Override
    public boolean addListener(EventListener listener) {
        return imageButton.getActor().addListener(listener);
    }

    @Override
    public boolean removeListener(EventListener listener) {
        return imageButton.getActor().removeListener(listener);
    }

    /**
     * Set the image button scene object to disabled.
     *
     * <p>
     *     When set to disabled, the button is not clickable.
     * </p>
     */
    public void setDisabled() {
        imageButton.setDisabled();
    }

    /**
     * Set the image button scene object to enabled.
     *
     * <p>
     *     When set to disabled, the button is not clickable.
     * </p>
     */
    public void setEnabled() {
        imageButton.setEnabled();
    }

    /**
     * Get a flag indicating whether the button is enabled {@code true} or not {@code false}.
     * @return boolean isEnabled()?
     */
    public boolean isEnabled() {
        return imageButton.isEnabled();
    }
}
