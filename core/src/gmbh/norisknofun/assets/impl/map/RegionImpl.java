package gmbh.norisknofun.assets.impl.map;

import com.badlogic.gdx.graphics.Color;

import gmbh.norisknofun.assets.AssetMap.Region;

/**
 * Default implementation of {@link Region}.
 */
final class RegionImpl implements Region {

    /**
     * Region's name.
     */
    private final String name;

    /**
     * Vertices defining the region's boundaries.
     */
    private final float[] vertices;

    /**
     * Name who owns the region.
     */
    private String regionOwner;

    /**
     * Number of troops positioned on this region.
     */
    private int numberTroops;

    /**
     * Region's color.
     */
    private Color regionColor;

    RegionImpl(String name, float[] vertices) {
        this.name = name;
        this.vertices = vertices;

        regionOwner = "none";
        numberTroops = 0;
        regionColor = Color.WHITE;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public float[] getVertices() {
        return vertices;
    }

    @Override
    public String getOwner() {
        return regionOwner;
    }

    @Override
    public void setOwner(String newOwner) {
        regionOwner = newOwner;
    }

    @Override
    public void setColor(Color color) {
        regionColor = color;
    }

    @Override
    public Color getColor() {
        return regionColor;
    }

    @Override
    public int getTroops() {
        return numberTroops;
    }

    /**
     * Set the amount of troops on this region
     * If there are none left, color it white again
     * @param amount new number of troops
     */
    @Override
    public void setTroops(int amount) {
        numberTroops = amount;
        if (numberTroops < 1) {
            regionColor = Color.WHITE;
        }
    }
}
