package gmbh.norisknofun.scene.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gmbh.norisknofun.assets.impl.map.AssetMap;
import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.scene.SceneBase;
import gmbh.norisknofun.scene.SceneNames;
import gmbh.norisknofun.scene.game.figures.Artillery;
import gmbh.norisknofun.scene.game.figures.Cavalry;
import gmbh.norisknofun.scene.game.figures.Figure;
import gmbh.norisknofun.scene.game.figures.Infantry;

/**
 * Main game scene.
 */
public final class GameScene extends SceneBase {

    private final GameData data;

    private GameObjectMap map;

    private BitmapFont font;
    private Label fontActor;
    private String currentRegion;

    private GameObjectMap gameObjectMap;

    private List<Figure> figures = new ArrayList<>();
    private Map<AssetMap.Region, PolygonRegion> regionMap;


    public GameScene(GameData data) {
        super(SceneNames.GAME_SCENE, Color.BLUE);
        this.data = data;
    }


    private void addFiguresToStage() {
        addSceneObject(createInfantry());

        addSceneObject(createCavalry());

        addSceneObject(createArtillery());
    }


    @Override
    public void show() {
        fontActor = initLabel();
        fontActor.setBounds(0, 0, 500, 100);
        getStage().addActor(fontActor);

        gameObjectMap = new GameObjectMap(data.getMapAsset());
        regionMap = gameObjectMap.getRegionMap();

        addSceneObject(gameObjectMap);
        addFiguresToStage();
        addInputListener();

        super.show();
    }

    private void addInputListener() {
   addSceneListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                for (int i = 0; i < figures.size(); i++) {
                    Figure actor = figures.get(i);

                    if (actor.isHighlighted() && isPointInRegion(x, y)) {
                        actor.addAction(Actions.moveTo(x, y, 0.2f));
                        actor.setHighlighted(false); // remove highlight after move
                    }
                }

                System.out.println("hallo stage :" + checkifPointisInoneRegion(x, y));
                return true;
            }

        });
    }

    private Infantry createInfantry() {
        Infantry infantry = new Infantry((int) (Gdx.graphics.getWidth() * 0.3), (int) (Gdx.graphics.getHeight() * 0.1), 200, 200);
        infantry.addTouchListener();

        figures.add(infantry);
        return infantry;
    }

    private Cavalry createCavalry() {
        Cavalry cavalry = new Cavalry((int) (Gdx.graphics.getWidth() * 0.5), (int) (Gdx.graphics.getHeight() * 0.1), 200, 200);
        cavalry.addTouchListener();

        figures.add(cavalry);
        return cavalry;
    }

    private Artillery createArtillery() {
        Artillery artillery = new Artillery((int) (Gdx.graphics.getWidth() * 0.7), (int) (Gdx.graphics.getHeight() * 0.1), 200, 200);
        artillery.addTouchListener();

        figures.add(artillery);
        return artillery;
    }

    /**
     * Same functionality as isPointInRegion, but with String return for debugging purposes
     *
     * @param pointx x coordinate
     * @param pointy y coordinate
     * @return Region name
     */
    private String checkifPointisInoneRegion(float pointx, float pointy) {
        String result = "Not in a Region";

        for (int i = 0; i < data.getMapAsset().getRegions().size(); i++) {
            float[] vertices = data.getMapAsset().getRegions().get(i).getVertices();
            if (Intersector.isPointInPolygon(vertices, 0, vertices.length, pointx / Gdx.graphics.getWidth(), pointy / Gdx.graphics.getHeight())) {
                result = "Region name:" + data.getMapAsset().getRegions().get(i).getName();
                currentRegion = data.getMapAsset().getRegions().get(i).getName();
                return result;
            }
        }
        currentRegion = "No Region.";
        return result;
    }

    /**
     * Checks if two specific coordinates are within a PolygonRegion
     *
     * @param pointX absolute value of X coordinate
     * @param pointY absolute value of Y coordinate
     * @return true if in a region, false if not.
     */
    private boolean isPointInRegion(float pointX, float pointY) {
        AssetMap.Region region;

        // for Intersector, we have to convert to percentual x/y coordinates. Simply divide by screen width/height
        for (int i = 0; i < data.getMapAsset().getRegions().size(); i++) {
            region = data.getMapAsset().getRegions().get(i);
            float[] vertices =region.getVertices();
            if (Intersector.isPointInPolygon(vertices, 0, vertices.length, pointX / Gdx.graphics.getWidth(), pointY / Gdx.graphics.getHeight())) {
                fontActor.setText("Region: " + region.getName());
                region.setOwner("Player");
                setRegionColor(Color.GREEN, region);
                return true;
            }

        }
        fontActor.setText("Region: None");
        return false;
    }

    private Label initLabel() {

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(3.5f);

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        style.fontColor = Color.WHITE;

        return new Label("Region: ", style);
    }

    private void setRegionColor(Color color, AssetMap.Region region) {
        Pixmap pix = new Pixmap(1,1, Pixmap.Format.RGBA8888);
        pix.setColor(Color.GREEN);
        pix.fill();
        Texture regionTexture = new Texture(pix);

        //gameObjectMap.getPolygonRegions().get(0).getRegion().setTexture(regionTexture);
        PolygonRegion polygonRegion = regionMap.get(region);
        polygonRegion.getRegion().setTexture(regionTexture);
    }

    @Override
    public void dispose() {


    }


}
