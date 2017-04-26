package gmbh.norisknofun.assets;

import java.io.InputStream;

/**
 * Interface for loading game assets.
 */
public interface AssetLoader<A extends Asset> {

    /**
     * Get the {@link AssetType} this loader can handle.
     */
    AssetType getAssetType();

    /**
     * Load the asset.
     *
     * @param stream The asset's input stream.
     *
     * @return Loaded asset or {@code null} if an error occurs.
     */
    A load(InputStream stream);
}
