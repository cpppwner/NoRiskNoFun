package gmbh.norisknofun.assets.impl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import gmbh.norisknofun.assets.AssetTexture;

/**
 * Asset for textures.
 */
final class AssetTextureImpl implements AssetTexture {

    /**
     * Cache for caching assets
     */
    private final LibGdxAssetCache cache;

    /**
     * Asset's filename.
     */
    private final String filename;

    /**
     * Wrapped libGdx texture.
     */
    private final Texture texture;

    /**
     * Create texture asset from given filename.
     */
    AssetTextureImpl(LibGdxAssetCache cache, String filename) {

        this.cache = cache;
        this.filename = filename;
        this.texture = cache.getTexture(filename);
    }

    @Override
    public String getName() {
        return filename;
    }

    @Override
    public void draw(Batch batch, float x, float y, float width, float height) {

        batch.draw(texture, x, y, width, height);
    }

    @Override
    public void dispose() {

        cache.releaseTexture(texture);
    }
}
