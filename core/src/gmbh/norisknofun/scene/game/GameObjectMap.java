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
import java.util.List;

import gmbh.norisknofun.assets.impl.map.AssetMap;
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

    private static final Color[] COLOR_POOL = new Color[]
    {
        Color.BLUE,
        Color.YELLOW,
        Color.ORANGE,
        Color.CYAN,
    };

    GameObjectMap(AssetMap assetMap) {

        this.assetMap = assetMap;
        batch = new PolygonSpriteBatch();
        shapeRenderer = new ShapeRenderer();

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(Color.WHITE);
        pix.fill();
        texture = new Texture(pix);

        initPolygonRegions(assetMap);
    }

    private void initPolygonRegions(AssetMap assetMap) {

        polygonRegions = new ArrayList<>(assetMap.getRegions().size());

/*        Pixmap pix = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pix.setColor(Color.FIREBRICK);
        pix.fill();
        Texture regionTexture = new Texture(pix);*/

        for (AssetMap.Region region : assetMap.getRegions()) {
            PolygonRegion polyReg = new PolygonRegion(new TextureRegion(texture),
                    region.getVertices(),
                    new EarClippingTriangulator().computeTriangles(region.getVertices()).toArray());
            /*polyReg.getRegion().setTexture(regionTexture);*/

            polygonRegions.add(polyReg);
        }
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.end();

        Gdx.gl.glLineWidth(3);
/*        Pixmap pix = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pix.setColor(Color.BROWN);
        pix.fill();
        Texture regionTexture = new Texture(pix);*/

        for (PolygonRegion region : polygonRegions) {
            /*region.getRegion().setTexture(regionTexture);*/

            this.batch.begin();
            this.batch.draw(region, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            this.batch.end();

            Polyline line = new Polyline(region.getVertices());
            line.setScale(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
            shapeRenderer.setColor(1.0f, 0.0f, 0.0f, 1.0f);
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
