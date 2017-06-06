package gmbh.norisknofun.assets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;

/**
 * Label asset.
 */
public interface AssetLabel extends Asset, Disposable {

    /**
     * Draw label.
     */
    void draw(Batch batch, float parentAlpha);

    /**
     * Get width in pixel.
     */
    float getWidth();

    /**
     * Get height in pixel.
     */
    float getHeight();

    /**
     * Set label's bounds.
     */
    void setBounds(float x, float y, float width, float height);

    /**
     * Set label's text.
     */
    void setText(String text);
}
