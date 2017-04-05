package gmbh.norisknofun;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Iterator;

/**
 * Created by pippp on 01.04.2017.
 */

public class GameScreen implements Screen, InputProcessor {

    
    private OrthographicCamera camera;
    private Texture img;
    private Rectangle bucket;
    private Vector3 touchpos;
    private Array<Rectangle> raindrops;
    private long lastDropTime;

    final NoRiskNoFun game;

    private Stage stage;
    private Viewport viewport;
    private MyActor actor;



    public GameScreen(final NoRiskNoFun game){
      this.game=game;
        try {
            img = new Texture(Gdx.files.internal("waterdrop.png"));

            camera = new OrthographicCamera();
            camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            viewport= new FitViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),camera);
            stage = new Stage(viewport);

            actor = new MyActor();
            actor.setBounds(800/2,20,64,64);
            touchpos = new Vector3();
//            raindrops = new Array<Rectangle>();
//            bucket = new Rectangle();
//            bucket.x = 800 / 2 - 64 / 2;
//            bucket.y = 20;
//            bucket.width = 64;
//            bucket.height = 64;
//            spawnRaindrop();
            stage.addActor(actor);
            stage.addListener(new InputListener(){
                @Override
                public boolean touchDown (InputEvent event, float x, float y, int pointer, int button){
                    System.out.println("hallo");


                    stage.getActors().get(0).addAction(Actions.moveTo(x-32,y-32,1));
                    return true;
                }
            });


        }catch (Exception e){
            Gdx.app.error("Error",e.getMessage(),e);
        }
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
       /* game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(img,bucket.x,bucket.y,64,64);
        for(Rectangle raindrop: raindrops){
            game.batch.draw(img,raindrop.x, raindrop.y,64,64);
        }
        game.batch.end();



        if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

        Iterator<Rectangle> iter = raindrops.iterator();
        while (iter.hasNext()){
            Rectangle raindrop = iter.next();
            raindrop.y-= 200 * Gdx.graphics.getDeltaTime();
            if(raindrop.y <0 ) iter.remove();
            if(raindrop.overlaps(bucket)) iter.remove();
        }*/
       stage.act();
       stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        img.dispose();
        stage.dispose();

    }

    private void spawnRaindrop(){
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, Gdx.graphics.getWidth()-64);
        raindrop.y = Gdx.graphics.getHeight();
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

//           touchpos.set(screenX,screenY,0);
//            camera.unproject(touchpos);
//            bucket.x = touchpos.x -64/2;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
