package gmbh.norisknofun.assets.impl.texture;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.io.InputStream;

import gmbh.norisknofun.assets.AssetLoader;
import gmbh.norisknofun.assets.AssetType;
import gmbh.norisknofun.assets.impl.map.AssetLoaderMap;

/**
 * Asset loader responsible for loading textures.
 */
public class AssetLoaderTexture implements AssetLoader<AssetTexture> {

    @Override
    public AssetType getAssetType() {
        return AssetType.ASSET_TYPE_TEXTURE;
    }

    @Override
    public AssetTexture load(String internalPath) {
        try {
            return new AssetTexture(new Texture(internalPath));
        } catch (Exception e) {
            Gdx.app.error(AssetLoaderMap.class.getSimpleName(), "Loading map failed", e);
            return null;
        }
    }

    @Override
    public AssetTexture load(InputStream stream) {
        throw new UnsupportedOperationException("Not implemented"); // for now
    }
}
