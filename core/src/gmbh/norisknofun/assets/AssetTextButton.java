package gmbh.norisknofun.assets;

import com.badlogic.gdx.utils.Disposable;

/**
 * Text button asset abstracting libgdx {@link com.badlogic.gdx.scenes.scene2d.ui.TextButton}.
 */
public interface AssetTextButton extends AssetWidget, Disposable {

    /**
     * Get text button's name.
     *
     * @return Button's name.
     */
    String getName();

    /**
     * Disable the text button.
     *
     * <p>
     *     When a button is disabled, it's not clickable.
     * </p>
     */
    void setDisabled();

    /**
     * Enable the text button.
     *
     * <p>
     *     When a button is enabled, it's clickable.
     * </p>
     */
    void setEnabled();

    /**
     * Test if the button is enabled.
     *
     * @return {@code true} if the button is enabled, {@code false} otherwise.
     */
    boolean isEnabled();
}
