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

    public GameScene(GameData data) {

        super(SceneNames.GAME_SCENE, new Color(0xFF));
        this.data = data;
    }

    @Override
    public void show() {

        AssetMap asset = data.getMapAsset();
    }
}
