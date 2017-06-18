package gmbh.norisknofun;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.assets.impl.LibGdxAssetFactory;
import gmbh.norisknofun.network.socket.SocketFactoryImpl;
import gmbh.norisknofun.scene.Scene;
import gmbh.norisknofun.scene.SceneData;
import gmbh.norisknofun.scene.SceneManager;
import gmbh.norisknofun.scene.SceneNames;
import gmbh.norisknofun.scene.game.ChooseTroopAmountScene;
import gmbh.norisknofun.scene.game.DiceRollScene;
import gmbh.norisknofun.scene.game.EndGameScene;
import gmbh.norisknofun.scene.game.GameScene;
import gmbh.norisknofun.scene.ui.CreateGameScene;
import gmbh.norisknofun.scene.ui.JoinGameScene;
import gmbh.norisknofun.scene.ui.LobbyScene;
import gmbh.norisknofun.scene.ui.MainMenuScene;
import gmbh.norisknofun.scene.ui.MapSelectionScene;

/**
 * Main entry point for the NoRiskNoFun game.
 */
public class NoRiskNoFun implements ApplicationListener  {

    /**
     * our own ip address.
     */
    private final String ip;

    private SceneData sceneData;

    public NoRiskNoFun(String ip) {
        this.ip=ip;
    }

    @Override
    public void create() {

        registerScenes();
        showStartupScene();
    }

    private void registerScenes() {

        sceneData = new SceneData(new LibGdxAssetFactory(), new SocketFactoryImpl());
        sceneData.setHostIp(ip);

        SceneManager.getInstance().registerScene(new MapSelectionScene(sceneData));
        SceneManager.getInstance().registerScene(new GameScene(sceneData));
        SceneManager.getInstance().registerScene(new CreateGameScene(sceneData));
        SceneManager.getInstance().registerScene(new JoinGameScene(sceneData));
        SceneManager.getInstance().registerScene(new MainMenuScene(sceneData));
        SceneManager.getInstance().registerScene(new DiceRollScene(sceneData));
        SceneManager.getInstance().registerScene(new LobbyScene(sceneData));
        SceneManager.getInstance().registerScene(new ChooseTroopAmountScene(sceneData));
        SceneManager.getInstance().registerScene(new EndGameScene(sceneData));
    }

    private void showStartupScene() {

        SceneManager.getInstance().setActiveScene(SceneNames.MAIN_MENU_SCENE);
    }

    @Override
    public void resize(int width, int height) {

        getActiveScene().resize(width, height);
    }

    @Override
    public void render() {

        sceneData.processPendingMessages();
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
