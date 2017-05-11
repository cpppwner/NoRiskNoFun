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
     * Load asset from Gdx internal path.
     *
     * @param internalPath Internal path local to Gdx internal location.
     *
     * @return Loaded asset or {@code null} if an error occurs.
     */
    A load(String internalPath);

    /**
     * Load the asset.
     *
     * @param stream The asset's input stream.
     *
     * @return Loaded asset or {@code null} if an error occurs.
     */
    A load(InputStream stream);
}
