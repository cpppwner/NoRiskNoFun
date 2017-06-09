package gmbh.norisknofun.assets.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import gmbh.norisknofun.assets.AssetTexture;

/**
 * Asset for textures.
 */
final class AssetTextureImpl implements AssetTexture {

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
    AssetTextureImpl(String filename) {

        this.filename = filename;
        this.texture = new Texture(Gdx.files.internal(filename));
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

        texture.dispose();
    }
}
