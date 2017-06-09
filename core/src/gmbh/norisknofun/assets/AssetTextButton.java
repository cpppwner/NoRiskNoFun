package gmbh.norisknofun.assets;

import com.badlogic.gdx.utils.Disposable;

/**
 * Text button asset abstracting libgdx {@link com.badlogic.gdx.scenes.scene2d.ui.TextButton}.
 */
public interface AssetTextButton extends UIAsset, Disposable {

    /**
     * Get text button's name.
     * @return
     */
    String getName();
}
