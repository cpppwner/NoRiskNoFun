package gmbh.norisknofun.assets;

import com.badlogic.gdx.utils.Disposable;

/**
 * Text button asset abstracting libgdx {@link com.badlogic.gdx.scenes.scene2d.ui.TextField}.
 */
public interface AssetTextField extends AssetWidget, Disposable {

    /**
     * Get text field's name.
     */
    String getName();

    /**
     * Get the text.
     */
    String getText();
}
