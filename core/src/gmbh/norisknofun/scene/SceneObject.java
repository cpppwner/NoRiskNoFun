package gmbh.norisknofun.scene;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;

/**
 * Scene Object base class.
 */
public abstract class SceneObject extends Group implements Disposable {

    protected SceneObject() {
        setTransform(false);
    }

    /**
     * Called when {@link Scene} gets shown.
     */
    void show() {

    }

    /**
     * Called when {@link Scene} gets hidden.
     */
    void hide() {

    }

    /**
     * Called when {@link Scene} gets resized.
     * @param width Width of the Scene.
     * @param height Height of the Scene.
     */
    void resize(int width, int height) {

    }

    @Override
    public void dispose() {

    }
}
