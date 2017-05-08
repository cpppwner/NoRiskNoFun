package gmbh.norisknofun.assets.impl.map;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gmbh.norisknofun.assets.Asset;

/**
 * Map asset.
 */
public class AssetMap implements Asset {

    private final String name;
    private final List<Region> regions;

    AssetMap(GameMap map) {
        this.name = map.name;
        this.regions = new ArrayList<>(map.regions.size());
        for (GameMap.Region region : map.regions)
            this.regions.add(createRegion(region, map));
    }

    private Region createRegion(GameMap.Region region, GameMap map) {
        float[] vertices = new float[region.vertexIndices.size() * 2];
        for (int i = 0; i < region.vertexIndices.size(); i++) {
            GameMap.Vertex vertex = map.vertices.get(region.vertexIndices.get(i));
            vertices[i * 2] = vertex.x;
            vertices[i * 2 + 1] = vertex.y;
        }

        return new Region(region.name, vertices);
    }

    @Override
    public String getName() {
        return name;
    }

    public List<Region> getRegions() {
        return Collections.unmodifiableList(regions);
    }

    public class Region
    {
        private final String name;
        private final float[] vertices;
        private String regionOwner;
        private int numberTroops;
        private Color regionColor;

        Region(String name, float[] vertices) {
            this.name = name;
            this.vertices = vertices;

            regionOwner = "none";
            numberTroops = 0;
            regionColor = Color.WHITE;
        }

        public String getName() {
            return name;
        }

        public float[] getVertices() {
            return vertices;
        }

        public String getOwner() {
            return regionOwner;
        }

        public void setOwner(String newOwner) {
            regionOwner = newOwner;
        }

        public void setColor(Color color) {
            regionColor = color;
        }

        public Color getColor() {
            return regionColor;
        }

        public int getTroops() {
            return numberTroops;
        }

        public void setTroops(int amount) {
            numberTroops = amount;
        }
    }
}
