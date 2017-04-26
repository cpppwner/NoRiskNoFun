package gmbh.norisknofun.assets.impl.map;

import java.util.List;

/**
 * POJO class used to deserialize a game map from JSON.
 */
class GameMap {

    /**
     * Vertex class representing a point in the map.
     */
    static class Vertex {
        float x;
        float y;
    }

    /**
     * Region class representing a region in the map.
     */
    static class Region {
        /**
         * Region's name, which must be unique.
         */
        String name;
        /**
         * List of indices in vertices, describing the region's points.
         */
        List<Integer> vertexIndices;

        /**
         * Neighbouring region names.
         */
        List<String> neighbouringRegions;
    }

    /**
     * Maps's name.
     */
    String name;

    /**
     * List of vertices used to define regions in the map.
     */
    List<Vertex> vertices;

    /**
     * List of regions in the game world.
     */
    List<Region> regions;
}
