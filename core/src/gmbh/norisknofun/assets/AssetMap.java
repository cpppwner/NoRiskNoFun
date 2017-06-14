package gmbh.norisknofun.assets;

import com.badlogic.gdx.graphics.Color;

import java.util.List;

/**
 * Map asset interface.
 */
public interface AssetMap extends Asset {

    /**
     * Get all regions defined in this map.
     */
    List<Region> getRegions();

    /**
     * Search for a region by it's name.
     *
     * @param regionName The region's name to search for.
     * @return The found {@link AssetMap.Region} or {@code null} if no region with given name exists.
     */
    Region getRegion(String regionName);

    /**
     * Region interface.
     *
     * <p>
     *     Region is part of a map.
     * </p>
     */
    public interface Region {

        /**
         * Get region's name.
         */
        String getName();

        /**
         * Get the vertices that define a region.
         *
         * <p>
         *     The list returned contains x and y coordinates for a poly line.
         *     All even indices are x coordinates and all odd indices the appropriate y coordinate.
         *
         *     E.g. the first point is defined (getVertices[0], getVertices[1]).
         *
         *     Note: All x/y coordinates are in range [0, 1].
         * </p>
         */
        float[] getVertices();

        /**
         * Get the player's name who owns the region.
         */
        String getOwner();

        /**
         * Set the player's name who owns the region or null, if no one currently does.
         * @param newOwner
         */
        void setOwner(String newOwner);

        void setColor(Color color);

        Color getColor();

        int getTroops();

        void setTroops(int amount);

        void updateTroops(int amount);

        void setNeighbouringRegions(List<String> regions);

        List<String> getNeighbouringRegions();
    }
}
