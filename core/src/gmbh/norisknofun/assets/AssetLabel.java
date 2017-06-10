package gmbh.norisknofun.assets;

import com.badlogic.gdx.utils.Disposable;

/**
 * Label asset.
 */
public interface AssetLabel extends AssetWidget, Disposable {

    /**
     * Get label's name.
     */
    String getName();

    /**
     * Set label's text.
     */
    void setText(String text);
}
