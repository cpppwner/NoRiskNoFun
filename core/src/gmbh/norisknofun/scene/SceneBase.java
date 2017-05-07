package gmbh.norisknofun.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.LinkedList;
import java.util.List;

/**
 * Base class for scenes.
 *
 * <p>
 *     Must be implemented by appropriate Scene classes.
 * </p>
 */
public abstract class SceneBase implements Scene {

    private final String sceneName;
    private final Color clearColor;
    private final List<SceneObject> sceneObjects;
    private final Stage stage;

    /**
     * Constructor taking scene name and color used to clear background.
     * @param sceneName Scene's name.
     * @param clearColor Color used to clear background.
     */
    protected SceneBase(String sceneName, Color clearColor) {

        this.sceneName = sceneName;
        this.clearColor = clearColor;
        sceneObjects = new LinkedList<>();
        stage = new Stage();
    }

    @Override
    public String getName() {

        return sceneName;
    }

    @Override
    public void addSceneObject(SceneObject sceneObject) {

        sceneObjects.add(sceneObject);
        stage.addActor(sceneObject);
    }


    public void addSceneListener(EventListener listener) {
        stage.addListener(listener);
    }

    public List<SceneObject> getSceneObjects() {
        return sceneObjects;
    }

    @Override
    public void preload() {

    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);
        for (SceneObject sceneObject : sceneObjects) {
            sceneObject.show();
        }
    }

    @Override
    public void hide() {

        Gdx.input.setInputProcessor(null);
        for (SceneObject sceneObject : sceneObjects) {
            sceneObject.hide();
        }
    }

    @Override
    public void resize(int width, int height) {

        stage.getViewport().update(width, height, true);
        for (SceneObject sceneObject : sceneObjects) {
            sceneObject.resize(width, height);
        }

    }

    @Override
    public void render (float delta) {

        Gdx.gl.glClearColor(clearColor.r, clearColor.g, clearColor.b, clearColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void pause () {

    }

    @Override
    public void resume () {

    }

    @Override
    public void dispose () {

        stage.dispose();
        for (SceneObject sceneObject : sceneObjects) {
            sceneObject.dispose();
        }
    }

    protected Camera getCamera() {

        return stage.getCamera();
    }

    protected Stage getStage() {

        return stage;
    }
}
