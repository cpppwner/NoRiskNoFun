package gmbh.norisknofun.scene.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.scene.SceneBase;
import gmbh.norisknofun.scene.SceneNames;
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


    public GameScene(GameData data) {
        super(SceneNames.GAME_SCENE, Color.BLUE);
        this.data = data;
    }


    private void addFiguresToStage() {
        addSceneObject(createInfantry());
        addSceneObject(createInfantry());
        addSceneObject(createInfantry());
        addSceneObject(createInfantry());
    }


    @Override
    public void show() {
        fontActor = initLabel();
        fontActor.setBounds(0, 0, 500, 100);
        getStage().addActor(fontActor);

        addSceneObject(new GameObjectMap(data.getMapAsset()));
        addFiguresToStage();

        addInputListener();

        super.show();
    }

    private void addInputListener() {
        getStage().addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                for (int i = 0; i < getSceneObjects().size(); i++) {
                    Actor actor = getSceneObjects().get(i);
                    if (actor.getClass() == Infantry.class) {
                        if (((Infantry) actor).isHighlighted() && isPointInRegion(x, y)) {
                            actor.addAction(Actions.moveTo(x, y, 0.2f));
                            ((Infantry) actor).setHighlighted(false); // remove highlight after move
                            fontActor.setText("Region: " + currentRegion);
                        }
                    }
                }

                System.out.println("hallo stage :" + checkifPointisInoneRegion(x, y));
                return true;
            }

        });
    }

    private Infantry createInfantry() {
        Infantry infantry = new Infantry((int) (Gdx.graphics.getWidth() * 0.5), (int) (Gdx.graphics.getHeight() * 0.1), 200, 200);
        final Infantry infantry1 = infantry;
        infantry.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                if (infantry1.isHighlighted()) {
                    infantry1.setHighlighted(false);
                } else {
                    infantry1.setHighlighted(true);
                }
                event.stop();
                System.out.println("hallo actor");
                return true;
            }
        });
        infantry = infantry1;
        return infantry;
    }

    /**
     * Same functionality as isPointInRegion, but with String return for debugging purposes
     *
     * @param pointx
     * @param pointy
     * @return
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

        // for Intersector, we have to convert to percentual x/y coordinates. Simply divide by screen width/height
        for (int i = 0; i < data.getMapAsset().getRegions().size(); i++) {
            float[] vertices = data.getMapAsset().getRegions().get(i).getVertices();
            if (Intersector.isPointInPolygon(vertices, 0, vertices.length, pointX / Gdx.graphics.getWidth(), pointY / Gdx.graphics.getHeight())) {
                return true;
            }

        }
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

    @Override
    public void dispose() {


    }


}
