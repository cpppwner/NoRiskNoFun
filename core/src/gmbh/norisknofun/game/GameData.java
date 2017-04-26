package gmbh.norisknofun.game;

import com.badlogic.gdx.Gdx;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import gmbh.norisknofun.assets.AssetLoaderFactory;
import gmbh.norisknofun.assets.impl.map.AssetMap;

/**
 * Class containing game related data.
 */
public class GameData {

    private final AssetLoaderFactory assetLoaderFactory;

    private String mapFilename = null;
    private AssetMap mapAsset = null;

    public GameData(AssetLoaderFactory assetLoaderFactory) {
        this.assetLoaderFactory = assetLoaderFactory;
    }

    public void setMapFile(String mapFilename) {
        this.mapFilename = mapFilename;
        mapAsset = null;
    }

    public AssetMap getMapAsset() {
        if (mapFilename == null)
            throw new IllegalStateException("mapFile was not set");

        if (mapAsset == null) {
            try (InputStream stream = Gdx.files.internal(mapFilename).read()) {
                mapAsset = assetLoaderFactory.createAssetLoaderMap().load(stream);
            } catch (Exception e) {
                Gdx.app.error("MAP", "Failed to load map asset", e);
            }
        }

        return mapAsset;
    }
}
