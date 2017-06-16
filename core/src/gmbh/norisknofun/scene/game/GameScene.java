package gmbh.norisknofun.scene.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import gmbh.norisknofun.assets.AssetMap;
import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.gamemessages.gui.MoveTroopGui;
import gmbh.norisknofun.game.gamemessages.gui.RemoveTroopGui;
import gmbh.norisknofun.game.gamemessages.gui.SpawnTroopGui;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.scene.Assets;
import gmbh.norisknofun.scene.SceneBase;
import gmbh.norisknofun.scene.SceneData;
import gmbh.norisknofun.scene.SceneNames;
import gmbh.norisknofun.scene.common.LabelSceneObject;
import gmbh.norisknofun.scene.common.SwitchSceneClickListener;
import gmbh.norisknofun.scene.common.TextButtonSceneObject;
import gmbh.norisknofun.scene.game.figures.Artillery;
import gmbh.norisknofun.scene.game.figures.Cavalry;
import gmbh.norisknofun.scene.game.figures.Figure;
import gmbh.norisknofun.scene.game.figures.Infantry;

/**
 * Main game scene.
 */
public final class GameScene extends SceneBase {

    private final SceneData sceneData;
    private final GameData data;

    private LabelSceneObject label;

    private List<Figure> figures = new ArrayList<>();
    private Map<AssetMap.Region, PolygonRegion> regionMap;
    private Map<String, AssetMap.Region> regionNameMap;
    private AssetMap.Region currentRegion;


    public GameScene(SceneData sceneData) {
        super(SceneNames.GAME_SCENE, Color.BLUE);
        this.sceneData = sceneData;
        this.data = sceneData.getGameData();

    }


    private void addFiguresToStage() {
        addSceneObject(createInfantry()); // This troop will be used to spawn new ones
    }

    @Override
    public void preload() {

        label = new LabelSceneObject(sceneData.createLabel("Region: ", Assets.FONT_36PX_WHITE_WITH_BORDER));
        label.setBounds(0, 0, 500, 100);
        addSceneObject(label);

        GameObjectMap  gameObjectMap = new GameObjectMap(data.getMapAsset());
        regionMap = gameObjectMap.getRegionMap();
        regionNameMap = gameObjectMap.getRegionNameMap();

        addSceneObject(gameObjectMap);
        addFiguresToStage();
        addInputListener();

        addRollButton();

        for (Player player:data.getPlayers()) {
            Gdx.app.log("GameScene", "Player Available: " + player.getPlayerName());
        }
    }

    @Override
    public void show() {
        // make sure the stage is not drawn again when coming back from another scene

        super.show();
    }

    private void addInputListener() {
        addSceneListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

                requestMoveActor(x, y);

                return true;
            }

        });
    }


    /**
     * Send a move request to the client state machine via GUI Messages
     * @param x X coordinate of the move
     * @param y Y coordinate of the move
     */
    private void requestMoveActor(float x, float y) {
        for (Figure actor : figures) {

            if (actor.isHighlighted() && isPointInRegion(x, y)) {

                // if it's the actor's first move, explicitly set the region
                if (actor.isFirstMove()) {
                    sceneData.sendMessageFromGui(new SpawnTroopGui(currentRegion.getName(), x, y,-1)); // id is 0 because we don't need it
                    actor.setHighlighted(false);
                    break;
                }

                // check if the actor moves out of its current region and set troops accordingly
                if (currentRegion != actor.getCurrentRegion()) {

                    //actor.getCurrentRegion().setTroops(actor.getCurrentRegion().getTroops()-1);

                    sceneData.sendMessageFromGui(new MoveTroopGui(actor.getCurrentRegion().getName(), currentRegion.getName(),actor.getId() ));
                }
            } else if (actor.isHighlighted() && !actor.isFirstMove()) { // todo: Temporary. If user moves figure out of region, it will be deleted.
                sceneData.sendMessageFromGui(new RemoveTroopGui(actor.getCurrentRegion().getName(), 1));
            }
        }
    }


    /**
     * Move a specific figure to given coordinates
     *
     * @param x Coordinate
     * @param y Coordinate
     * @param actor Figure to move
     */
    private void moveActor(float x, float y, Figure actor) {
        actor.addAction(Actions.moveTo(x - (actor.getWidth() / 2), y - (actor.getHeight() / 2), 0.2f));
        actor.setHighlighted(false); // remove highlight after move
    }

    private void moveActorToRegion(String region, Figure actor) {
        Vector2 movePosition = calculatePolygonCentroid(regionNameMap.get(region).getVertices());

        actor.addAction(Actions.moveTo(movePosition.x * Gdx.graphics.getWidth(), movePosition.y * Gdx.graphics.getHeight(), 0.2f));
        actor.setHighlighted(false);
    }

    private Infantry createInfantry() {
        Infantry infantry = new Infantry(Gdx.graphics.getWidth() * 0.0f, Gdx.graphics.getHeight() * 0.15f, 200, 200,-1 );
        infantry.addTouchListener();

        figures.add(infantry);
        return infantry;
    }

    private Cavalry createCavalry() {
        Cavalry cavalry = new Cavalry(Gdx.graphics.getWidth() * 0.5f, Gdx.graphics.getHeight() * 0.1f, 200, 200, -1);
        cavalry.addTouchListener();

        figures.add(cavalry);
        return cavalry;
    }

    private Artillery createArtillery() {
        Artillery artillery = new Artillery(Gdx.graphics.getWidth() * 0.7f, Gdx.graphics.getHeight() * 0.1f, 200, 200, -1);
        artillery.addTouchListener();

        figures.add(artillery);
        return artillery;
    }

    /**
     * Checks if two specific coordinates are within a PolygonRegion
     * and sets currentRegion to this region
     *
     * @param pointX absolute value of X coordinate
     * @param pointY absolute value of Y coordinate
     * @return true if in a region, false if not.
     */
    private boolean isPointInRegion(float pointX, float pointY) {

        for (AssetMap.Region region : data.getMapAsset().getRegions()) {
            currentRegion = region;
            float[] vertices = region.getVertices();

            // for Intersector, we have to convert to percentual x/y coordinates. Simply divide by screen width/height
            if (Intersector.isPointInPolygon(vertices, 0, vertices.length, pointX / Gdx.graphics.getWidth(), pointY / Gdx.graphics.getHeight())) {
                label.setText("Region: " + region.getName());
                //region.setOwner(data.getCurrentPlayer().getPlayerName());

                return true;
            }

        }
        label.setText("Region: None");
        return false;
    }

    /**
     * Re-color a given region
     * @param color New Color for the Region
     * @param region Region to re-color
     */
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
        rollButton = new TextButtonSceneObject(sceneData.createTextButton("Dice Roll", Assets.DICE_CHEATS_TEXT_BUTTON_DESCRIPTOR), null);
        rollButton.setBounds(1000, 100, 500, 100);
        rollButton.addListener(new SwitchSceneClickListener(SceneNames.DICE_SCENE));
        addSceneObject(rollButton);
    }

    /**
     * Calculate the centroid of a polygon defined by its vertices
     * @param vertices Array of polygon vertices
     * @return Vector2 containing x and y coordinates of the centroid
     */
    private Vector2 calculatePolygonCentroid(float[] vertices) {
        Vector2 polygonCentroid = new Vector2();

        GeometryUtils.polygonCentroid(vertices, 0, vertices.length, polygonCentroid);
        return polygonCentroid;
    }

    /**
     * Remove a certain amount of troops from a region given in message
     * @param message GUI Message indicating which troops to remove
     */
    private void removeTroop(RemoveTroopGui message) {
        int amount = message.getTroopAmount();
        Gdx.app.log("Removing", message.getRegionName() + ", " + message.getTroopAmount());
        List<Figure> figuresToRemove = new LinkedList<>(); // use a separate list to avoid ConcurrentModificationException on figures list

        for (Figure actor : figures) {
            if (actor.getId() == -1) { // one of the three default figures. ignore
                continue;
            }
            if (amount <= 0) { // stop if the correct actors have been removed
                break;
            }

            // remove actor if it's on the same region
            if (actor.getCurrentRegion().getName().equals(message.getRegionName())) {
                figuresToRemove.add(actor);
                amount--;
            }
        }

        removeFigures(figuresToRemove);

        if (amount != 0) {
            // todo: something wrong, there weren't enough troops on the region
            Gdx.app.log("GameScene", "Couldn't remove all requested troops");
        }
    }

    /**
     * Remove a list of Figure from the game
     * @param figuresToRemove List containing all Figure objects to remove
     */
    private void removeFigures(List<Figure> figuresToRemove) {
        for (Figure actor:figuresToRemove) {
            actor.getCurrentRegion().updateTroops(-1);
            figures.remove(actor);
            actor.dispose();
            actor.remove();
        }
    }

    /**
     * Remove a specific actor from the game and update the underlying region
     * @param actor Actor to remove
     */
    private void removeFigure(Figure actor) {
        actor.getCurrentRegion().updateTroops(-1);
        figures.remove(actor);
        
        actor.dispose();
        actor.remove();
    }

    /**
     * Creates a new Infantry on the coordinates specified within the message
     * @param message GUI Message containing position information
     */
    private void spawnNewTroop(SpawnTroopGui message) {

        AssetMap.Region region = regionNameMap.get(message.getRegionName());
        Vector2 troopCoordinates = calculatePolygonCentroid(region.getVertices());

        Infantry infantry = new Infantry((troopCoordinates.x * Gdx.graphics.getWidth()) - 100, (troopCoordinates.y * Gdx.graphics.getHeight()) - 100, 200, 200, message.getId());
        infantry.addTouchListener();
        infantry.setFirstMove(false);
        infantry.setCurrentRegion(regionNameMap.get(message.getRegionName()));


        // todo: don't create a new Color object every time
        //setRegionColor(new Color(data.getCurrentPlayer().getColor()), infantry.getCurrentRegion());
        setRegionColor(Color.BROWN, infantry.getCurrentRegion());

        figures.add(infantry);
        addSceneObject(infantry);
    }

    /**
     * Move the highlighted Figures to the position specified in the message
     * @param message GUI Message containing information about the changes
     */
    private void moveTroop(MoveTroopGui message) {
        AssetMap.Region toRegion = regionNameMap.get(message.getToRegion());
        AssetMap.Region fromRegion = regionNameMap.get(message.getFromRegion());
        for (Figure actor: figures) {

            if (actor.getId()==message.getFigureId()) {
                moveActorToRegion(message.getToRegion(), actor);
                actor.setCurrentRegion(toRegion);
                setRegionColor(Color.BROWN, toRegion);

                // update the troop amounts
                fromRegion.setTroops(fromRegion.getTroops() - 1);
                toRegion.setTroops(toRegion.getTroops() + 1);

            }
        }
    }

    /**
     * Check if a flag in GameData has been changed and update the GUI accordingly
     */
    private void handleGuiUpdate() {
        Message message = data.getGuiChanges();

        if (message.getType().equals(SpawnTroopGui.class)) {
            // spawn troop
            spawnNewTroop((SpawnTroopGui) data.getGuiChanges());
        } else if (message.getType().equals(MoveTroopGui.class)) {
            // move existing troop
            moveTroop((MoveTroopGui) message);
        } else if (message.getType().equals(RemoveTroopGui.class)) {
            removeTroop((RemoveTroopGui) message);
        }
        else {
            Gdx.app.log("GameScene","Unknown Message: " + message.getClass().getSimpleName());
        }
    }

    @Override
    public void render(float delta) {

        if (data.hasChanged()) {
            handleGuiUpdate();
            data.setChangedFlag(false);
        }

        super.render(delta);
    }
}
