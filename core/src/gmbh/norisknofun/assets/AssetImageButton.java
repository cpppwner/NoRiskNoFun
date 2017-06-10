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
}
