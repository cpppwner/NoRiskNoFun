package gmbh.norisknofun.assets.impl;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import gmbh.norisknofun.assets.AssetPixmapTexture;

/**
 * Implementation of {@link AssetPixmapTexture}.
 */
class AssetPixmapTextureImpl implements AssetPixmapTexture {


    private final LibGdxAssetCache cache;
    private final Color color;
    private final Texture texture;

    AssetPixmapTextureImpl(LibGdxAssetCache cache, Color color) {

        this.cache = cache;
        this.color = color;
        this.texture = cache.getPixMapTexture(color);
    }

    @Override
    public String getName() {

        return "Pixmap" + color.toString();
    }

    @Override
    public Texture getTexture() {

        return texture;
    }

    @Override
    public void dispose() {

        cache.releasePixMapTexture(texture);
    }
}
