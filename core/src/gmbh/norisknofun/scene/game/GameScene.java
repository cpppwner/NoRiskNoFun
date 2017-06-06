package gmbh.norisknofun.scene.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import gmbh.norisknofun.assets.AssetMap;
import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.scene.SceneBase;
import gmbh.norisknofun.scene.SceneManager;
import gmbh.norisknofun.scene.SceneNames;
import gmbh.norisknofun.scene.common.LabelSceneObject;
import gmbh.norisknofun.scene.common.TextButtonSceneObject;
import gmbh.norisknofun.scene.game.figures.Artillery;
import gmbh.norisknofun.scene.game.figures.Cavalry;
import gmbh.norisknofun.scene.game.figures.Figure;
import gmbh.norisknofun.scene.game.figures.Infantry;

/**
 * Main game scene.
 */
public final class GameScene extends SceneBase {

    private final GameData data;

    private BitmapFont font;
    private LabelSceneObject label;
    private boolean initializeScene = true;

    private List<Figure> figures = new ArrayList<>();
    private Map<AssetMap.Region, PolygonRegion> regionMap;
    private AssetMap.Region currentRegion;



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
        // make sure the stage is not drawn again when coming back from another scene
        if (initializeScene) {
            GameObjectMap gameObjectMap;

            label = initLabel();
            label.setBounds(0, 0, 500, 100);
            addSceneObject(label);

            gameObjectMap = new GameObjectMap(data.getMapAsset());
            regionMap = gameObjectMap.getRegionMap();

            addSceneObject(gameObjectMap);
            addFiguresToStage();
            addInputListener();

            addRollButton();

            initializeScene = false;
        }
        super.show();
    }

    private void addInputListener() {
        addSceneListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                moveActor(x, y);

                return true;
            }

        });
    }

    /**
     * Called by scene input listener to move highlighted actors to the click position
     * @param x coordinate
     * @param y coordinate
     */
    private void moveActor(float x, float y) {
        for (int i = 0; i < figures.size(); i++) {
            Figure actor = figures.get(i);

            if (actor.isHighlighted() && isPointInRegion(x, y)) {
                actor.addAction(Actions.moveTo(x - (actor.getWidth() / 2), y - (actor.getHeight() / 2), 0.2f));
                actor.setHighlighted(false); // remove highlight after move

                // if it's the actors first move, explicitly set the region
                if (actor.isFirstMove()) {
                    actor.setCurrentRegion(currentRegion);
                    currentRegion.setTroops(currentRegion.getTroops() + 1);
                    actor.setFirstMove(false);
                }

                // check if the actor moves out of its current region and set troops accordingly
                if (currentRegion != actor.getCurrentRegion()) {
                    int troops = actor.getCurrentRegion().getTroops() - 1;
                    actor.getCurrentRegion().setTroops(troops);
                    currentRegion.setTroops(currentRegion.getTroops() + 1);

                    // re-color the old region, if there are no more troops it will become white
                    setRegionColor(actor.getCurrentRegion().getColor(), actor.getCurrentRegion());
                    actor.setCurrentRegion(currentRegion);
                }

                // also update the color of the new region as we moved onto it
                currentRegion.setColor(Color.GREEN);
                setRegionColor(currentRegion.getColor(), currentRegion);
            }
        }
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
            currentRegion = region;
            float[] vertices = region.getVertices();

            if (Intersector.isPointInPolygon(vertices, 0, vertices.length, pointX / Gdx.graphics.getWidth(), pointY / Gdx.graphics.getHeight())) {
                label.getLabel().setText("Region: " + region.getName());
                region.setOwner("Player");

                return true;
            }

        }
        label.getLabel().setText("Region: None");
        return false;
    }

    private LabelSceneObject initLabel() {

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(3.5f);

        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;
        style.fontColor = Color.WHITE;

        return new LabelSceneObject(new Label("Region: ", style));
    }

    private void setRegionColor(Color color, AssetMap.Region region) {
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(color);
        pix.fill();

        Texture regionTexture = new Texture(pix);
        PolygonRegion polygonRegion = regionMap.get(region);
        polygonRegion.getRegion().setTexture(regionTexture);
    }

    /**
     * Create a button to switch to the dice roll scene
     */
    private void addRollButton() {
        TextButtonSceneObject rollButton;

        rollButton = createButton("Dice Roll");
        rollButton.setBounds(1000, 100, 500, 100);
        rollButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                SceneManager.getInstance().setActiveScene(SceneNames.DICE_SCENE);
            }
        });
        addSceneObject(rollButton);
    }


    /**
     * Create a button scene object with the given text
     * @param buttonText Text on the button
     * @return new TextButtonSceneObject
     */
    private TextButtonSceneObject createButton(String buttonText) {

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.up = new TextureRegionDrawable(new TextureRegion(new Texture("button.png")));
        style.down = new TextureRegionDrawable(new TextureRegion(new Texture("button.png")));
        style.fontColor = new Color(0.9f, 0.5f, 0.5f, 1);
        style.downFontColor = new Color(0, 0.4f, 0, 1);

        return new TextButtonSceneObject(new TextButton(buttonText, style));
    }
}
