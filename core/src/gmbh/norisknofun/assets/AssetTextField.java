package gmbh.norisknofun.assets;

import com.badlogic.gdx.utils.Disposable;

/**
 * Text button asset abstracting libgdx {@link com.badlogic.gdx.scenes.scene2d.ui.TextField}.
 */
public interface AssetTextField extends AssetWidget, Disposable {

    /**
     * Get text field's name.
     *
     * @return Text field's name.
     */
    String getName();

    /**
     * Get the text.
     *
     * @return Text currently entered in this text field.
     */
    String getText();
}
