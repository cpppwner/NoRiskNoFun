package gmbh.norisknofun.assets;

/**
 * Abstract factory to create appropriate assets.
 *
 * <p>
 *     Makes testing of GUI scenes easier.
 * </p>
 */
public interface AssetFactory {

    /**
     * Create map asset.
     *
     * @param filename The map filename which is local to assets directory.
     * @return Newly created map asset.
     */
    AssetMap createAssetMap(String filename);

    /**
     * Create texture asset.
     *
     * @param filename The map filename which is local to assets directory.
     * @return Newly created texture asset.
     */
    AssetTexture createAssetTexture(String filename);

    /**
     * Create sound asset.
     *
     * @param filename The map filename which is local to assets directory.
     * @return Newly created sound asset.
     */
    AssetSound createAssetSound(String filename);
}
