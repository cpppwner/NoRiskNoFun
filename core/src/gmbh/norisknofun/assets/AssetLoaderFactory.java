package gmbh.norisknofun.assets;

import gmbh.norisknofun.assets.impl.map.AssetLoaderMap;

/**
 * Abstract factory for creating appropriate {@link AssetLoader}.
 */
public interface AssetLoaderFactory {

    AssetLoader<Asset> createAssetLoader(AssetType type);

    AssetLoaderMap createAssetLoaderMap();
}
