package gmbh.norisknofun.assets.impl.map;

import java.io.InputStream;

import gmbh.norisknofun.assets.Asset;
import gmbh.norisknofun.assets.AssetLoader;
import gmbh.norisknofun.assets.AssetType;

/**
 * Asset loader for loading game maps.
 */
public class AssetLoaderMap implements AssetLoader {

    @Override
    public AssetType getAssetType() {

        return AssetType.ASSET_TYPE_MAP;
    }

    @Override
    public Asset load(InputStream stream) {
        return null;
    }
}
