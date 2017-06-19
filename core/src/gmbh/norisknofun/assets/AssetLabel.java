package gmbh.norisknofun.assets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Disposable;

/**
 * Label asset.
 */
public interface AssetLabel extends AssetWidget, Disposable {

    /**
     * Get label's name.
     *
     * @return Get label's name.
     */
    String getName();

    /**
     * Set label's text.
     *
     * @param text labeltext
     */
    void setText(String text);

    /**
     * Set alignment.
     *
     * @param alignment alignment you want set
     */
    void setTextAlignment(int alignment);

    /**
     * Set label's background color.
     *
     * @param color backgroundcolor
     */
    void setBackgroundColor(Color color);
}
