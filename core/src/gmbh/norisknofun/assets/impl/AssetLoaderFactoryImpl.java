package gmbh.norisknofun.assets.impl;

import gmbh.norisknofun.assets.Asset;
import gmbh.norisknofun.assets.AssetLoader;
import gmbh.norisknofun.assets.AssetLoaderFactory;
import gmbh.norisknofun.assets.AssetType;
import gmbh.norisknofun.assets.impl.map.AssetLoaderMap;

/**
 * Default implementation of {@link AssetLoaderFactory}.
 */
public class AssetLoaderFactoryImpl implements AssetLoaderFactory {

    @Override
    public AssetLoader<Asset> createAssetLoader(AssetType type) {

        switch(type) {

            case ASSET_TYPE_MAP:
                return createAssetLoaderMap();
            default:
                throw new IllegalArgumentException("Unknown asset type");
        }
    }

    @Override
    public AssetLoaderMap createAssetLoaderMap() {

        return new AssetLoaderMap();
    }
}
