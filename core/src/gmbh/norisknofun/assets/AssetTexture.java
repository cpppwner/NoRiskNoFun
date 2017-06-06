package gmbh.norisknofun.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;

/**
 * Texture asset.
 *
 * <p>
 *     Use our own interface for easier testing.
 * </p>
 */
public interface AssetTexture extends Asset, Disposable {

    /**
     * Draw this texture asset.
     *
     * @param batch Gdx batch for drawing.
     * @param x X-coordinate where to draw this texture to.
     * @param y Y-coordinate where to draw this texture to.
     * @param width Width of the texture to draw.
     * @param height Height of the texture to draw.
     */
    void draw(Batch batch, float x, float y, float width, float height);


}
