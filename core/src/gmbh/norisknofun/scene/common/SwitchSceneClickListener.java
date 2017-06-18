package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import gmbh.norisknofun.scene.SceneManager;

/**
 * {@link ClickListener} used to switch scene.
 */
public class SwitchSceneClickListener extends ClickListener {

    /**
     * Scene's name to switch to when clicked event occured.
     */
    private final String nextSceneName;

    /**
     * Init listener with scene's name to which to switch.
     */
    public SwitchSceneClickListener(String nextSceneName) {

        super();
        this.nextSceneName = nextSceneName;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {

        SceneManager.getInstance().setActiveScene(nextSceneName);
    }
}
