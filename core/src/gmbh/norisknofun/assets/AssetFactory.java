package gmbh.norisknofun.assets;

import com.badlogic.gdx.graphics.Color;

/**
 * Abstract factory to create appropriate assets.
 *
 * <p>
 *     Makes testing of GUI scenes easier.
 * </p>
 */
public interface AssetFactory {

    /**
     * Create map asset.
     *
     * @param filename The map filename which is local to assets directory.
     * @return Newly created map asset.
     */
    AssetMap createAssetMap(String filename);

    /**
     * Create texture asset.
     *
     * @param filename The map filename which is local to assets directory.
     * @return Newly created texture asset.
     */
    AssetTexture createAssetTexture(String filename);

    /**
     * Create sound asset.
     *
     * @param filename The map filename which is local to assets directory.
     * @return Newly created sound asset.
     */
    AssetSound createAssetSound(String filename);

    /**
     * Create a label asset using the default font (see assets/font).
     *
     * @param text The label text.
     * @param fontSize Font size.
     * @param color Color used for the text.
     * @param borderWidth Width of the border wrapping the text.
     * @return Newly created Label asset.
     */
    AssetLabel createAssetLabel(String text, int fontSize, Color color, float borderWidth);
}
