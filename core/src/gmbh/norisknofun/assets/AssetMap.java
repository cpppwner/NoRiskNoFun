package gmbh.norisknofun.assets;

import com.badlogic.gdx.graphics.Color;

import java.util.List;

/**
 * Map asset interface.
 */
public interface AssetMap extends Asset {

    /**
     * Get all regions defined in this map.
     *
     * @return Immutable list of map regions.
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
    interface Region {

        /**
         * Get region's name.
         *
         * @return Region's name.
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
         *
         * @return Vertices as array.
         */
        float[] getVertices();

        /**
         * Get the player's name who owns the region.
         *
         * @return Get the owner (player name) of this region.
         */
        String getOwner();

        /**
         * Set the player's name who owns the region or null, if no one currently does.
         */
        void setOwner(String newOwner);

        /**
         * Set region's color.
         */
        void setColor(Color color);

        /**
         * Get region's color.
         *
         * @return Color of the region, which is the owner's color.
         */
        Color getColor();

        /**
         * Get number of troops on this region.
         *
         * @return Number of troops positioned on this region.
         */
        int getTroops();

        /**
         * Set number of troops on this region.
         */
        void setTroops(int amount);

        /**
         * Updates the amount of troops.
         * @param amount The number of troops to add (can be negative as well).
         */
        void updateTroops(int amount);

        /**
         * Get all neighbouring regions.
         *
         * @return Immutable list of neighbouring regions.
         */
        List<String> getNeighbouringRegions();
    }
}
