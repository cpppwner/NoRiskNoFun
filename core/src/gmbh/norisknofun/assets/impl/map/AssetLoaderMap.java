package gmbh.norisknofun.assets.impl.map;

import com.badlogic.gdx.Gdx;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

/**
 * Asset loader for loading game maps.
 */
public final class AssetLoaderMap {

    /**
     * Load {@link AssetMapImpl} from given filename.
     *
     * <p>
     *     This method just reads the file contents and forwards to {@link AssetLoaderMap#load(InputStream)}.
     * </p>
     *
     * @param internalPath The internal path from where to load the file.
     * @return Loaded map or {@code null} if loading failed.
     */
    public AssetMapImpl load(String internalPath) {
        try (InputStream stream = Gdx.files.internal(internalPath).read()) {
            return load(stream);
        } catch (Exception e) {
            Gdx.app.error(AssetLoaderMap.class.getSimpleName(), "Loading map failed", e);
            return null;
        }
    }

    /**
     * Load {@link AssetMapImpl} from given stream.
     *
     * @param stream The stream from which to load the map.
     * @return Loaded map or {@code null} if loading failed.
     */
    AssetMapImpl load(InputStream stream) {

        if (stream == null)
            throw new IllegalArgumentException("stream");

        try (Reader reader = new InputStreamReader(stream)) {
            GameMap map = new Gson().fromJson(reader, GameMap.class);
            return createAsset(map);
        } catch (Exception e) {
            Gdx.app.error(AssetLoaderMap.class.getSimpleName(), "Loading map failed", e);
            return null;
        }
    }

    /**
     * Transform {@link GameMap} to {@link AssetMapImpl} and also do some sanity checks.
     *
     * @param map The loaded map.
     * @return Transformed map asset.
     */
    private static AssetMapImpl createAsset(GameMap map) {
        checkMapConsistency(map);
        return new AssetMapImpl(map);
    }

    /**
     * Runs some validation checks on loaded map.
     *
     * @param map The map to check for consistency.
     */
    private static void checkMapConsistency(GameMap map) {

        if (map.name == null || map.name.trim().isEmpty())
            throw new IllegalStateException("map without a name");
        if (map.vertices == null || map.vertices.isEmpty())
            throw new IllegalStateException("map without vertices");
        if (map.regions == null || map.regions.isEmpty())
            throw new IllegalStateException("map without regions");

        Set<String> regionNameSet = new HashSet<>();
        for (GameMap.Region region : map.regions) {
            if (!regionNameSet.add(region.name))
                throw new IllegalStateException("duplicate region \"" + region.name + "\"");
            validateRegionVertices(region, map);
        }

        for (GameMap.Region region : map.regions) {
            if (!regionNameSet.containsAll(region.neighbouringRegions))
                throw new IllegalStateException("Invalid neighbours for region \"" + region.name + "\"");
        }
    }

    /**
     * Check region consistency.
     *
     * @param region The region to check.
     * @param map Loaded map.
     */
    private static void validateRegionVertices(GameMap.Region region, final GameMap map) {

        if (region.vertexIndices.size() < 4)
            throw new IllegalStateException("min 3 vertices per region");
        if (!region.vertexIndices.get(0).equals(region.vertexIndices.get(region.vertexIndices.size() - 1)))
            throw new IllegalStateException("first and last vertex must be the same for region \"" + region.name + "\"");

        for (int index : region.vertexIndices) {
            if (index < 0 || index >= map.vertices.size())
                throw new IllegalStateException("invalid vertex index for region \"" + region.name + "\"");
        }
    }
}
