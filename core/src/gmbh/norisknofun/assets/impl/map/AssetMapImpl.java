package gmbh.norisknofun.assets.impl.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gmbh.norisknofun.assets.AssetMap;

/**
 * Default implementation of {@link AssetMap}.
 */
final class AssetMapImpl implements AssetMap {

    /**
     * Map's name.
     */
    private final String name;

    /**
     * List of region's for this map.
     */
    private final List<Region> regions;

    /**
     * Constructor.
     * @param map The {@link GameMap} used for initializing this asset.
     */
    AssetMapImpl(GameMap map) {
        this.name = map.name;
        this.regions = new ArrayList<>(map.regions.size());
        for (GameMap.Region region : map.regions)
            this.regions.add(createRegion(region, map));
    }

    /**
     * Helper method to create a {@link AssetMap.Region} out ouf {@link GameMap.Region}.
     */
    private Region createRegion(GameMap.Region region, GameMap map) {
        float[] vertices = new float[region.vertexIndices.size() * 2];
        for (int i = 0; i < region.vertexIndices.size(); i++) {
            GameMap.Vertex vertex = map.vertices.get(region.vertexIndices.get(i));
            vertices[i * 2] = vertex.x;
            vertices[i * 2 + 1] = vertex.y;
        }

        return new RegionImpl(region.name, vertices);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Region> getRegions() {
        return Collections.unmodifiableList(regions);
    }

    @Override
    public Region getRegion(String regionName){

        for(Region region: regions){
            if(region.getName().equals(regionName)) {
                return region;
            }
        }
        return null;
    }
}
