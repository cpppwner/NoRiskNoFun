package gmbh.norisknofun.gmbh.norisknofun.assets;

import gmbh.norisknofun.gmbh.norisknofun.assets.gmbh.norisknofun.assets.impl.gmbh.norisknofun.assets.impl.map.AssetLoaderMap;

/**
 * Abstract factory for creating appropriate {@link AssetLoader}.
 */
public interface AssetLoaderFactory {

    AssetLoader<Asset> createAssetLoader(AssetType type);

    AssetLoaderMap createAssetLoaderMap();
}
