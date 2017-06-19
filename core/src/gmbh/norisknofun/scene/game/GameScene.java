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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import gmbh.norisknofun.assets.AssetMap;
import gmbh.norisknofun.assets.AssetModalDialog;
import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.gamemessages.gui.MoveTroopGui;
import gmbh.norisknofun.game.gamemessages.gui.ActionDoneGui;
import gmbh.norisknofun.game.gamemessages.gui.RemoveTroopGui;
import gmbh.norisknofun.game.gamemessages.gui.SpawnTroopGui;
import gmbh.norisknofun.game.gamemessages.gui.UpdateCurrentPlayerGui;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.scene.Assets;
import gmbh.norisknofun.scene.SceneBase;
import gmbh.norisknofun.scene.SceneData;
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

    private final SceneData sceneData;
    private final GameData data;

    private LabelSceneObject clickedRegionLabel;
    private LabelSceneObject turnIndicator;
    private LabelSceneObject currentStateLabel;
    TextButtonSceneObject doneButton;


    private List<Figure> figures = new ArrayList<>();
    private Map<String, LabelSceneObject> troopIndicators;
    private Map<AssetMap.Region, PolygonRegion> regionMap;
    private Map<String, AssetMap.Region> regionNameMap;
    private AssetMap.Region tappedRegion;

    private Color ownColor;

    public GameScene(SceneData sceneData) {
        super(SceneNames.GAME_SCENE, Color.DARK_GRAY);
        this.sceneData = sceneData;
        this.data = sceneData.getGameData();

    }


    private void addFiguresToStage() {
        addSceneObject(createInfantry()); // This troop will be used to spawn new ones
    }

    @Override
    public void preload() {

        clickedRegionLabel = new LabelSceneObject(sceneData.createLabel("Region: ", Assets.FONT_36PX_WHITE_WITH_BORDER));
        clickedRegionLabel.setBounds(0, 0, 500, 100);
        addSceneObject(clickedRegionLabel);

        GameObjectMap  gameObjectMap = new GameObjectMap(data.getMapAsset());
        regionMap = gameObjectMap.getRegionMap();
        regionNameMap = gameObjectMap.getRegionNameMap();
        troopIndicators = new HashMap<>();
        ownColor = new Color(data.getMyself().getColor());

        addSceneObject(gameObjectMap);
        addFiguresToStage();
        addInputListener();

        initTurnIndicator();
        initStateIndicator();
        initTroopIndicators();
        initDoneButton();

        for (Player player:data.getPlayers()) {
            Gdx.app.log("GameScene", "Player Available: " + player.getPlayerName() + " Color: " + player.getColor());
        }

        Gdx.app.log("GameScene", "Myself: " + data.getMyself().getPlayerName() + " Color: " + data.getMyself().getColor());

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

                requestTroopAction(x, y);

                return true;
            }

        });
    }

    /**
     * Initialize labels indicating the amount of troops on each region
     */
    private void initTroopIndicators() {
        for (AssetMap.Region region : data.getMapAsset().getRegions()) {
            Vector2 labelPos = calculatePolygonCentroid(region.getVertices());
            LabelSceneObject label = new LabelSceneObject(sceneData.createLabel("0", Assets.FONT_110PX_WHITE_WITH_BORDER));
            label.setBounds(labelPos.x * Gdx.graphics.getWidth(), labelPos.y * Gdx.graphics.getHeight(), label.getWidth(), label.getHeight());
            addSceneObject(label);
            troopIndicators.put(region.getName(), label);
        }
    }

    private void initDoneButton() {
        doneButton = new TextButtonSceneObject(sceneData.createTextButton("Skip", Assets.DEFAULT_TEXT_BUTTON_DESCRIPTOR), null);
        doneButton.setBounds(Gdx.graphics.getWidth() - doneButton.getWidth(), 0, 200, 100);
        doneButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                sceneData.sendMessageFromGui(new ActionDoneGui());
            }
        });
        addSceneObject(doneButton);
        doneButton.setVisible(false); // only show when Player is in ChooseTargetState
    }

    /**
     * Send a move request to the client state machine via GUI Messages
     * @param x X coordinate of the move
     * @param y Y coordinate of the move
     */
    private void requestTroopAction(float x, float y) {
        for (Figure actor : figures) {

            if (actor.isHighlighted() && isPointInRegion(x, y)) {

                // if it's the actor's first move, explicitly set the region
                if (actor.isFirstMove()) {
                    sceneData.sendMessageFromGui(new SpawnTroopGui(tappedRegion.getName(), -1)); // id is -1 because we don't need it
                    actor.setHighlighted(false);
                    break;
                }

                // check if the actor moves out of its current region and set troops accordingly
                if (tappedRegion != actor.getCurrentRegion()) {

                    //actor.getCurrentRegion().setTroops(actor.getCurrentRegion().getTroops()-1);
                    Gdx.app.log("Move Troop", "Moving troop: " + actor.getId());
                    actor.setHighlighted(false);
                    sceneData.sendMessageFromGui(new MoveTroopGui(actor.getCurrentRegion().getName(), tappedRegion.getName(),actor.getId()));
                }
            } else if (actor.isHighlighted() && !actor.isFirstMove()) { // todo: Temporary. If user moves figure out of region, it will be deleted.
                sceneData.sendMessageFromGui(new RemoveTroopGui(actor.getCurrentRegion().getName(), 1));
                actor.setHighlighted(false);
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

    private void initTurnIndicator() {
        turnIndicator = new LabelSceneObject(sceneData.createLabel("Your Turn!", Assets.FONT_110PX_WHITE_WITH_BORDER));
        turnIndicator.setBackgroundColor(ownColor);
        addSceneObject(turnIndicator);
        turnIndicator.setBounds(0, Gdx.graphics.getHeight() - turnIndicator.getHeight(), turnIndicator.getWidth(), turnIndicator.getHeight());
    }

    /**
     * Indicate which state the player is currently in
     */
    private void initStateIndicator() {
        currentStateLabel = new LabelSceneObject(sceneData.createLabel("Waiting...", Assets.FONT_60PX_WHITE_WITH_BORDER));
        addSceneObject(currentStateLabel);
        currentStateLabel.setBounds(Gdx.graphics.getWidth() - currentStateLabel.getWidth() - 200, Gdx.graphics.getHeight() - currentStateLabel.getHeight(), currentStateLabel.getWidth(), currentStateLabel.getHeight());
    }


    /**

    /**
     * Checks if two specific coordinates are within a PolygonRegion
     * and sets tappedRegion to this region
     *
     * @param pointX absolute value of X coordinate
     * @param pointY absolute value of Y coordinate
     * @return true if in a region, false if not.
     */
    private boolean isPointInRegion(float pointX, float pointY) {

        for (AssetMap.Region region : data.getMapAsset().getRegions()) {
            tappedRegion = region;
            float[] vertices = region.getVertices();

            // for Intersector, we have to convert to percentual x/y coordinates. Simply divide by screen width/height
            if (Intersector.isPointInPolygon(vertices, 0, vertices.length, pointX / Gdx.graphics.getWidth(), pointY / Gdx.graphics.getHeight())) {
                clickedRegionLabel.setText("Region: " + region.getName());

                return true;
            }

        }
        clickedRegionLabel.setText("Region: None");
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
            data.setChangedFlag(true);
            figures.remove(actor);
            actor.dispose();
            actor.remove();
        }
    }

    /**
     * Remove a specific Figure from the game and update the underlying region
     * @param actor Actor to remove
     */
    private void removeFigure(Figure actor) {
        actor.getCurrentRegion().updateTroops(-1);
        data.setChangedFlag(true);
        figures.remove(actor);
        
        actor.dispose();
        actor.remove();
    }

    /**
     * Creates a new Infantry on the Region specified within the message
     * @param message GUI Message containing position information
     */
    private void spawnNewTroop(SpawnTroopGui message) {

        AssetMap.Region region = regionNameMap.get(message.getRegionName());
        Vector2 troopCoordinates = calculatePolygonCentroid(region.getVertices());

        Infantry infantry = new Infantry((troopCoordinates.x * Gdx.graphics.getWidth()) - 50, (troopCoordinates.y * Gdx.graphics.getHeight()) - 50, 100, 100, message.getId());
        infantry.addTouchListener();
        infantry.setFirstMove(false);
        infantry.setCurrentRegion(regionNameMap.get(message.getRegionName()));


        // set color and owner only when the first troop spawns on this region
        if (region.getOwner().equals("none")) {
            region.setOwner(data.getCurrentPlayer().getPlayerName());
            setRegionColor(new Color(data.getCurrentPlayer().getColor()), infantry.getCurrentRegion());
        }
        region.updateTroops(1);
        data.setChangedFlag(true);
        Gdx.app.log("Region Spawn", region.getTroops() + " Troops on " + region.getName() + " Owner: " + region.getOwner());

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
                setRegionColor(new Color(data.getCurrentPlayer().getColor()), toRegion);

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
        Message message = data.getGuiChanges().poll();

        if (message.getType().equals(SpawnTroopGui.class)) {
            Gdx.app.log("GameScene","Received: " + message.getClass().getSimpleName());
            spawnNewTroop((SpawnTroopGui) message);
        } else if (message.getType().equals(MoveTroopGui.class)) {
            Gdx.app.log("GameScene","Received: " + message.getClass().getSimpleName());
            moveTroop((MoveTroopGui) message);
        } else if (message.getType().equals(RemoveTroopGui.class)) {
            Gdx.app.log("GameScene","Received: " + message.getClass().getSimpleName());

            removeTroop((RemoveTroopGui) message);
        } else if (message.getType().equals(UpdateCurrentPlayerGui.class)) {
            Gdx.app.log("GameScene","Received: " + message.getClass().getSimpleName());

            data.setCurrentPlayer(((UpdateCurrentPlayerGui) message).getCurrentPlayer());
        }
        else {
            Gdx.app.log("GameScene","Unknown Message: " + message.getClass().getSimpleName());
        }
    }

    private void checkErrors() {
        // check for errors and display popup
        String error = data.getLastError();
        if (error != null) {
            AssetModalDialog dialog = sceneData.createModalDialog(error, Assets.ERROR_DIALOG_DESCRIPTOR);
            dialog.show(getStage());
            dialog.setBounds(getStage().getWidth() / 4.0f, getStage().getHeight() / 4.0f,
                    getStage().getWidth() / 2.0f, getStage().getHeight() / 2.0f);
        }
    }

    /**
     * Update all troop indicators
     */
    private void updateTroopIndicators() {
        LabelSceneObject label;
        for (AssetMap.Region region : data.getMapAsset().getRegions()) {
            label = troopIndicators.get(region.getName());
            label.setText(Integer.toString(region.getTroops()));
            label.setZIndex(label.getZIndex() + 1);

        }
    }

    @Override
    public void render(float delta) {

        if (data.hasChanged()) {
            while (!data.getGuiChanges().isEmpty()) {
                handleGuiUpdate();
            }

            if (data.getGuiChanges().isEmpty()) {
                data.setChangedFlag(false);
            }

            // show whose turn it is
            if (data.isMyTurn()) {
                turnIndicator.setVisible(true);
            } else {
                turnIndicator.setVisible(false);
            }

            // show the current state
            currentStateLabel.setText(data.getCurrentStateName());

            updateTroopIndicators();

            if (data.getCurrentStateName().equals("ChooseTarget") || data.getCurrentStateName().equals("MoveTroops")) {
                doneButton.setVisible(true);
            } else {
                doneButton.setVisible(false);
            }

        }

        checkErrors();

        super.render(delta);
    }
}
