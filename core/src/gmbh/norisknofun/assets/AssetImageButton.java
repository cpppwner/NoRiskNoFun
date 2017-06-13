package gmbh.norisknofun.assets;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Disposable;

/**
 * Image button asset, wrapping libgdx {@link ImageButton}.
 */
public interface AssetImageButton extends AssetWidget, Disposable {

    /**
     * Get image button's name.
     */
    String getName();

    /**
     * Disable the image button.
     *
     * <p>
     *     When a button is disabled, it's not clickable.
     * </p>
     */
    void setDisabled();

    /**
     * Enable the image button.
     *
     * <p>
     *     When a button is enabled, it's clickable.
     * </p>
     */
    void setEnabled();

    /**
     * Test if the button is enabled.
     */
    boolean isEnabled();
}
