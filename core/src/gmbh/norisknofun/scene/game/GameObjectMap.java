package gmbh.norisknofun.scene.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.List;

import gmbh.norisknofun.assets.impl.map.AssetMap;

/**
 * Map game object.
 */
class GameObjectMap extends Actor {

    private final AssetMap assetMap;
    private PolygonSpriteBatch batch;
    private Texture texture;

    private List<PolygonRegion> polygonRegions;

    GameObjectMap(AssetMap assetMap) {

        this.assetMap = assetMap;
        batch = new PolygonSpriteBatch();

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(Color.WHITE);
        pix.fill();
        texture = new Texture(pix);

        initPolygonRegions(assetMap);
    }

    private void initPolygonRegions(AssetMap assetMap) {

        polygonRegions = new ArrayList<>(assetMap.getRegions().size());

        for (AssetMap.Region region : assetMap.getRegions()) {
            PolygonRegion polyReg = new PolygonRegion(new TextureRegion(texture),
                    region.getVertices(),
                    new EarClippingTriangulator().computeTriangles(region.getVertices()).toArray());

            polygonRegions.add(polyReg);
        }
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.end();

        Gdx.gl.glLineWidth(3);

        for (PolygonRegion region : polygonRegions) {

            this.batch.begin();
            this.batch.draw(region, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            this.batch.end();

            Polyline line = new Polyline(region.getVertices());
            line.setScale(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            ShapeRenderer renderer = new ShapeRenderer();
            renderer.begin(ShapeRenderer.ShapeType.Line);
            renderer.setProjectionMatrix(batch.getProjectionMatrix());
            renderer.setColor(1.0f, 0.0f, 0.0f, 1.0f);
            renderer.polyline(line.getTransformedVertices());
            renderer.end();
        }

        batch.begin();
    }
}
