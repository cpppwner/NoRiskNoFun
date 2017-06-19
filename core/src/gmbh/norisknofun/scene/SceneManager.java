package gmbh.norisknofun.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Simple scene manager class.
 *
 * <p>
 *     This class is responsible for storing scenes, managing the active scene
 *     and managing scene transitions.
 * </p>
 *
 * <p>
 *     The class itself is implemented as Singleton, since only one instance must exist
 *     in the whole game.
 * </p>
 */
public class SceneManager implements Disposable {

    /**
     * The sole instance of the {@link SceneManager}.
     */
    private static final SceneManager instance = new SceneManager();

    /**
     * Map storing all registered {@link Scene scenes}.
     */
    private final Map<String, SceneInfo> scenes;

    /**
     * Current active scene.
     */
    private Scene activeScene;

    /**
     * Default constructor.
     *
     * <p>
     *     Private, because of Singleton pattern.
     * </p>
     */
    private SceneManager() {
        scenes = new HashMap<>();
        activeScene = new NullScene();
    }

    /**
     * Get the sole instance of {@link SceneManager}.
     *
     * @return instance
     */
    public static SceneManager getInstance() {

        return instance;
    }

    /**
     * Registers and pre loads a new scene.
     *
     * @param scene The scene to register and preload.
     * @return {@code true} if scene could be registered, else {@code false}.
     */
    public boolean registerScene(Scene scene) {

        if (scene == null)
            throw new IllegalArgumentException("scene is null");

        if (scenes.containsKey(scene.getName()))
            return false;

        scenes.put(scene.getName(), new SceneInfo(scene));

        return true;
    }

    /**
     * Unregisters a scene by it's name and calls the {@link Scene#dispose()}.
     *
     * @param sceneName Scene's name to unregister.
     * @return {@code true} if scene could be unregistered, else {@code false}.
     */
    public boolean unregisterScene(String sceneName) {

        if (sceneName == null)
            throw new IllegalArgumentException();

        if (!scenes.containsKey(sceneName))
            return false;

        if (activeScene.getName().equals(sceneName))
            // scene is the active on, scene transition must be done first.
            return false;

        SceneInfo scene = scenes.remove(sceneName);
        scene.dispose();

        return true;
    }

    /**
     * Change the active scene.
     *
     * <p>
     *     A scene with given sceneName must have been registered first.
     * </p>
     *
     * @param sceneName The scene's name of the new active scene.
     *
     *
     * @return {@code true} if scene could be switched, else {@code false}.
     */
    public boolean setActiveScene(String sceneName) {

        if (sceneName == null)
            throw new IllegalArgumentException();

        if (!scenes.containsKey(sceneName))
            return false;

        if (activeScene.getName().equals(sceneName))
            // scene is the active on, no transition needs to be done
            return true;

        activeScene.hide();
        SceneInfo nextScene = scenes.get(sceneName);
        nextScene.preload();
        activeScene = nextScene.scene;
        activeScene.show();
        activeScene.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        return true;
    }

    /**
     * Get the active scene.
     *
     * @return Scene
     */
    public Scene getActiveScene() {

        return activeScene;
    }

    /**
     * Get a set containing registered scene names.
     *
     * @return set registered names
     */
    public Set<String> getRegisteredScenes() {

        return Collections.unmodifiableSet(scenes.keySet());
    }

    @Override
    public void dispose() {

        activeScene.hide();
        activeScene = new NullScene(); // set back to null scene

        for (SceneInfo sceneInfo : scenes.values()) // don't replace with lambda expression
            sceneInfo.dispose();
        scenes.clear();
    }

    /**
     * Dummy scene used to avoid null checks.
     *
     * <p>
     *     See also NullObject pattern.
     * </p>
     */
    private static final class NullScene implements Scene {

        @Override
        public String getName() {
            return "";
        }

        @Override
        public void addSceneObject(SceneObject sceneObject) { /*NullObject pattern*/ }

        @Override
        public void preload() { /*NullObject pattern*/ }

        @Override
        public void show() { /*NullObject pattern*/ }

        @Override
        public void render(float delta) { /*NullObject pattern*/ }

        @Override
        public void resize(int width, int height) { /*NullObject pattern*/ }

        @Override
        public void pause() { /*NullObject pattern*/ }

        @Override
        public void resume() { /*NullObject pattern*/ }

        @Override
        public void hide() { /*NullObject pattern*/ }

        @Override
        public void dispose() { /*NullObject pattern*/ }
    }

    /**
     * Helper class to store some additional infos.
     */
    private static final class SceneInfo implements Disposable {

        private boolean isPreloaded = false;
        private final Scene scene;

        private SceneInfo(Scene scene) {
            this.scene = scene;
        }

        @Override
        public void dispose() {
            scene.dispose();
        }

        private void preload() {
            if (!isPreloaded) {
                scene.preload();
                isPreloaded = true;
            }
        }
    }
}
