package gmbh.norisknofun;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

/**
 * Created by pippp on 01.04.2017.
 */

public class GameScreen implements Screen {

    
    private OrthographicCamera camera;
    private Texture img;
    private Rectangle bucket;
    private Vector3 touchpos;
    private Array<Rectangle> raindrops;
    private long lastDropTime;

    final NoRiskNoFun game;


    public GameScreen(final NoRiskNoFun game){
      this.game=game;
        img = new Texture(Gdx.files.internal("waterdrop.png"));

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        
        touchpos= new Vector3();
        raindrops = new Array<Rectangle>();
        bucket = new Rectangle();
        bucket.x = 800 / 2 - 64 / 2;
        bucket.y = 20;
        bucket.width = 64;
        bucket.height = 64;
        spawnRaindrop();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.draw(img,bucket.x,bucket.y,64,64);
        for(Rectangle raindrop: raindrops){
            game.batch.draw(img,raindrop.x, raindrop.y,64,64);
        }
        game.batch.end();

        if(Gdx.input.isTouched()){
            touchpos.set(Gdx.input.getX(),Gdx.input.getY(),0);
            camera.unproject(touchpos);
            bucket.x = touchpos.x -64/2;
        }

        if(TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRaindrop();

        Iterator<Rectangle> iter = raindrops.iterator();
        while (iter.hasNext()){
            Rectangle raindrop = iter.next();
            raindrop.y-= 200 * Gdx.graphics.getDeltaTime();
            if(raindrop.y <0 ) iter.remove();
            if(raindrop.overlaps(bucket)) iter.remove();
        }

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

    }

    private void spawnRaindrop(){
        Rectangle raindrop = new Rectangle();
        raindrop.x = MathUtils.random(0, 800-64);
        raindrop.y = 480;
        raindrop.width = 64;
        raindrop.height = 64;
        raindrops.add(raindrop);
        lastDropTime = TimeUtils.nanoTime();
    }
}
