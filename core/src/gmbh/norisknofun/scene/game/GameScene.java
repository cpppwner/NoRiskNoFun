package gmbh.norisknofun.scene.game;

import com.badlogic.gdx.graphics.Color;

import gmbh.norisknofun.assets.impl.map.AssetMap;
import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.scene.SceneBase;
import gmbh.norisknofun.scene.SceneNames;

/**
 * Main game scene.
 */
public final class GameScene extends SceneBase {

    private final GameData data;

    private GameObjectMap map;

    public GameScene(GameData data) {

        super(SceneNames.GAME_SCENE, Color.BLACK);
        this.data = data;
    }

    @Override
    public void show() {

        getStage().clear();
        getStage().addActor(new GameObjectMap(data.getMapAsset()));
    }

    @Override
    public void dispose() {


    }

}
