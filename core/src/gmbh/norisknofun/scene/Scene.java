package gmbh.norisknofun.scene;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Interface for all scenes.
 */
public interface Scene extends Screen {

    /**
     * Get scene's name.
     * @return Scene's name.
     */
    String getName();

    /**
     * Add {@link SceneObject} to this {@link Scene}.
     *
     * @param sceneObject The scene object to add.
     */
    void addSceneObject(SceneObject sceneObject);

    /**
     * Called by {@link SceneManager} when this scene gets registered.
     */
    void preload();
}
