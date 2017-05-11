package gmbh.norisknofun.assets.impl;

import gmbh.norisknofun.assets.Asset;
import gmbh.norisknofun.assets.AssetLoader;
import gmbh.norisknofun.assets.AssetLoaderFactory;
import gmbh.norisknofun.assets.AssetType;
import gmbh.norisknofun.assets.impl.map.AssetLoaderMap;
import gmbh.norisknofun.assets.impl.texture.AssetLoaderTexture;

/**
 * Default implementation of {@link AssetLoaderFactory}.
 */
public class AssetLoaderFactoryImpl implements AssetLoaderFactory {

    @Override
    public AssetLoader<? extends Asset> createAssetLoader(AssetType type) {

        switch(type) {

            case ASSET_TYPE_MAP:
                return createAssetLoaderMap();
            case ASSET_TYPE_TEXTURE:
                return createAssetLoaderTexture();
            default:
                throw new IllegalArgumentException("Unknown asset type");
        }
    }

    @Override
    public AssetLoaderMap createAssetLoaderMap() {

        return new AssetLoaderMap();
    }

    @Override
    public AssetLoaderTexture createAssetLoaderTexture() {

        return new AssetLoaderTexture();
    }
}
