package gmbh.norisknofun;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.assets.impl.AssetLoaderFactoryImpl;
import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.scene.Scene;
import gmbh.norisknofun.scene.SceneManager;
import gmbh.norisknofun.scene.SceneNames;
import gmbh.norisknofun.scene.ui.CreateGameScene;
import gmbh.norisknofun.scene.game.GameScene;
import gmbh.norisknofun.scene.ui.JoinGameScene;
import gmbh.norisknofun.scene.ui.MapSelectionScene;

public class NoRiskNoFun implements ApplicationListener  {

    @Override
    public void create() {

        registerScenes();
        showStartupScene();
    }

    private void registerScenes() {

        GameData gameData = new GameData(new AssetLoaderFactoryImpl());
        SceneManager.getInstance().registerScene(new MapSelectionScene(gameData));
        SceneManager.getInstance().registerScene(new GameScene(gameData));
        SceneManager.getInstance().registerScene(new CreateGameScene(gameData));
        SceneManager.getInstance().registerScene(new JoinGameScene(gameData));
    }

    private void showStartupScene() {

        SceneManager.getInstance().setActiveScene(SceneNames.MAP_SELECTION_SCENE
        );
    }

    @Override
    public void resize(int width, int height) {

        getActiveScene().resize(width, height);
    }

    @Override
    public void render() {

        getActiveScene().render(Gdx.graphics.getDeltaTime());
    }

    @Override
    public void pause() {

        getActiveScene().pause();
    }

    @Override
    public void resume() {

        getActiveScene().resume();
    }

    @Override
    public void dispose() {

        SceneManager.getInstance().dispose();
    }

    private Scene getActiveScene() {

        return SceneManager.getInstance().getActiveScene();
    }
}