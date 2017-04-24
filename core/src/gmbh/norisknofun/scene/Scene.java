package gmbh.norisknofun.scene;

import com.badlogic.gdx.Screen;

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
     * Called by {@link SceneManager} when this scene gets registered.
     */
    void preload();
}
