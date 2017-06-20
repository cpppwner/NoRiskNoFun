package gmbh.norisknofun.scene.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Polyline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gmbh.norisknofun.assets.AssetMap;
import gmbh.norisknofun.scene.SceneObject;

/**
 * Map game object.
 */
class GameObjectMap extends SceneObject {

    private final AssetMap assetMap;
    private PolygonSpriteBatch batch;
    private Texture texture;
    private ShapeRenderer shapeRenderer;

    private List<PolygonRegion> polygonRegions;
    private Map<AssetMap.Region, PolygonRegion> regionMap;
    private Map<String, AssetMap.Region> regionNameMap;

    GameObjectMap(AssetMap assetMap) {

        this.assetMap = assetMap;
        batch = new PolygonSpriteBatch();
        shapeRenderer = new ShapeRenderer();

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(Color.WHITE);
        pix.fill();
        texture = new Texture(pix);

        regionMap = new HashMap<>();
        regionNameMap = new HashMap<>();

        initPolygonRegions();
    }

    private void initPolygonRegions() {

        polygonRegions = new ArrayList<>(assetMap.getRegions().size());


        for (AssetMap.Region region : assetMap.getRegions()) {
            PolygonRegion polyReg = new PolygonRegion(new TextureRegion(texture),
                    region.getVertices(),
                    new EarClippingTriangulator().computeTriangles(region.getVertices()).toArray());

            regionMap.put(region, polyReg);
            regionNameMap.put(region.getName(), region);
            polygonRegions.add(polyReg);
        }
    }

    public Map<AssetMap.Region, PolygonRegion> getRegionMap() {
        return regionMap;
    }

    public Map<String, AssetMap.Region> getRegionNameMap() {
        return regionNameMap;
    }

    public List<PolygonRegion> getPolygonRegions() {
        return polygonRegions;
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.end();

        Gdx.gl.glLineWidth(10);

        for (PolygonRegion region : polygonRegions) {

            this.batch.begin();
            this.batch.draw(region, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            this.batch.end();

            Polyline line = new Polyline(region.getVertices());
            line.setScale(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.setColor(Color.BROWN);
            shapeRenderer.polyline(line.getTransformedVertices());
            shapeRenderer.end();
        }

        batch.begin();
    }

    @Override
    public void dispose() {

        batch.dispose();
        shapeRenderer.dispose();

    }
}
