package gmbh.norisknofun.scene.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.Random;

import gmbh.norisknofun.NoRiskNoFun;


public class DiceRoll implements Screen {

    private NoRiskNoFun game;

    private long lastShakeTime;
    private String diceRollText;
    private int[] rollResults = {1, 1, 1};
    private int cheatsAvailable = 3;
    private boolean hasBeenShaken = false;
    private boolean canRoll = true;

    private static final float GRAVITY_EARTH = 9.80665f;
    private static final float SHAKE_GRAVITY_THRESHOLD = 2.0f;

    private OrthographicCamera camera;
    private Texture[] dieTextures;
    private Image dieFirst;
    private Image dieSecond;
    private Image dieThird;
    private Image buttonBack;

    private Stage stage;
    private Viewport viewport;

    int SCREEN_HEIGHT = 1920;
    int SCREEN_WIDTH = 1080;

    public DiceRoll(NoRiskNoFun game) {
        this.game = game;

        // temporary button to end this screen.
        Texture btnBackTexture = new Texture(Gdx.files.internal("badlogic.jpg"));
        buttonBack = new Image(btnBackTexture);
        buttonBack.setX(SCREEN_WIDTH-256);
        buttonBack.setY(SCREEN_HEIGHT-256);

        //TODO: use spritesheets and proper animation
        Texture die1 = new Texture(Gdx.files.internal("dice/dieWhite1.png"));
        Texture die2 = new Texture(Gdx.files.internal("dice/dieWhite2.png"));
        Texture die3 = new Texture(Gdx.files.internal("dice/dieWhite3.png"));
        Texture die4 = new Texture(Gdx.files.internal("dice/dieWhite4.png"));
        Texture die5 = new Texture(Gdx.files.internal("dice/dieWhite5.png"));
        Texture die6 = new Texture(Gdx.files.internal("dice/dieWhite6.png"));
        dieTextures = new Texture[]{die1, die2, die3, die4, die5, die6};

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
        viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT, camera);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        dieFirst = new Image(die1);
        dieSecond = new Image(die1);
        dieThird = new Image(die1);

        dieFirst.setX(10);
        dieFirst.setY(10);
        dieFirst.setScale(6);
        dieSecond.setX(410);
        dieSecond.setY(10);
        dieSecond.setScale(6);
        dieThird.setX(810);
        dieThird.setY(10);
        dieThird.setScale(6);

        setDiceClickListener();

        stage.addActor(dieFirst);
        stage.addActor(dieSecond);
        stage.addActor(dieThird);
        stage.addActor(buttonBack);
    }

    /**
     * Check if player can cheat and do so if it's available.
     * @param die Image of the die to re-roll
     * @param index index of the die to re-roll
     */
    private void tryCheat(Image die, int index) {
        if (cheatsAvailable > 0) {
            diceRoll(index);
            showRollResult(die, index);
            cheatsAvailable--;
            System.out.println("[DEBUG] Cheat successful. " + cheatsAvailable + " remaining.");
        } else {
            System.out.println("[DEBUG] No cheats remaining.");
        }
    }

    /**
     * Set ClickListener to all die.
     * Used for cheat function.
     */
    private void setDiceClickListener() {
        dieFirst.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("dieFirst PRESSED!");
                tryCheat(dieFirst, 0);
                System.out.printf("[DEBUG] %d, %d, %d\n", rollResults[0], rollResults[1], rollResults[2]);
                return true;
            }
        });
        dieSecond.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("dieSecond PRESSED!");
                tryCheat(dieSecond, 1);
                System.out.printf("[DEBUG] %d, %d, %d\n", rollResults[0], rollResults[1], rollResults[2]);
                return true;
            }
        });
        dieThird.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("dieThird PRESSED!");
                tryCheat(dieThird, 2);
                System.out.printf("[DEBUG] %d, %d, %d\n", rollResults[0], rollResults[1], rollResults[2]);
                return true;
            }
        });

        // temporary clickListener for back button
        buttonBack.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("buttonBack PRESSED!");
                //game.setScreen(new MenuScreen(game));
                return true;
            }
        });
    }

    /**
     * "Animate" the dice roll
     */
    private void randomizeDice() {
        Random rnd = new Random();
        rnd.setSeed(TimeUtils.nanoTime());

        dieFirst.setDrawable(new SpriteDrawable(new Sprite(dieTextures[rnd.nextInt(6)])));
        dieSecond.setDrawable(new SpriteDrawable(new Sprite(dieTextures[rnd.nextInt(6)])));
        dieThird.setDrawable(new SpriteDrawable(new Sprite(dieTextures[rnd.nextInt(6)])));
    }

    /**
     * Animation for only a single die. Used for cheat function.
     *
     * @param die Image you want to animate.
     */
    private void randomizeDice(Image die) {
        Random rnd = new Random();
        rnd.setSeed(TimeUtils.nanoTime());

        die.setDrawable(new SpriteDrawable(new Sprite(dieTextures[rnd.nextInt(6)])));
    }

    /**
     * Check if the device is currently being shaken
     * @return
     */
    private boolean hasShaken() {
        // get current gravity readings of accelerometer axes in relation to earth's gravity
        float xGrav = Gdx.input.getAccelerometerX() / GRAVITY_EARTH;
        float yGrav = Gdx.input.getAccelerometerY() / GRAVITY_EARTH;
        float zGrav = Gdx.input.getAccelerometerZ() / GRAVITY_EARTH;

        // gForce will be close to 1 when there is no movement.
        float gForce = (float) Math.sqrt((xGrav * xGrav) + (yGrav * yGrav) + (zGrav * zGrav));

        if (gForce > SHAKE_GRAVITY_THRESHOLD) {
            return true;
        }
        return false;
    }

    /**
     * Roll all dice and update their results
     */
    private void diceRoll() {
        Random rnd = new Random();
        rnd.setSeed(TimeUtils.nanoTime());

        // generate a random number from 1-6
        for (int i = 0; i < rollResults.length; i++) {
            rollResults[i] = rnd.nextInt(6) + 1;
        }
    }

    /**
     * Roll only a single die and update the result
     *
     * @param index die to roll
     */
    private void diceRoll(int index) {
        Random rnd = new Random();
        rnd.setSeed(TimeUtils.nanoTime());

        rollResults[index] = rnd.nextInt(6) + 1;
    }

    /**
     * Set the images to actual roll results
     */
    private void showRollResult() {
        dieFirst.setDrawable(new SpriteDrawable(new Sprite(dieTextures[rollResults[0] - 1])));
        dieSecond.setDrawable(new SpriteDrawable(new Sprite(dieTextures[rollResults[1] - 1])));
        dieThird.setDrawable(new SpriteDrawable(new Sprite(dieTextures[rollResults[2] - 1])));
    }

    /**
     * Set the images to the roll result of a specific die
     *
     * @param die   die to change
     * @param index which result it should get
     */
    private void showRollResult(Image die, int index) {
        die.setDrawable(new SpriteDrawable(new Sprite(dieTextures[rollResults[index] - 1])));
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        // millis() should be precise enough for this. No need for nanoTime()
        if ((hasShaken() || hasBeenShaken) && canRoll) {


            // only update if it hasn't been shaken in the last 2 seconds
            if (TimeUtils.millis() - lastShakeTime > 5000) {
                diceRoll();
                System.out.printf("[DEBUG] %d, %d, %d\n", rollResults[0], rollResults[1], rollResults[2]);
                diceRollText = "SHAKEN!";
                lastShakeTime = TimeUtils.millis();
                hasBeenShaken = true;
            }

            // randomize results for 2 seconds after a shake
            if (TimeUtils.millis() - lastShakeTime < 2000) {
                randomizeDice();
            } else {
                showRollResult();
                hasBeenShaken = false;
                canRoll = false;
            }
        }

        if (TimeUtils.millis() - lastShakeTime > 5000) {
            diceRollText = "SHAKE ME!";
        }

        stage.act();
        stage.draw();

/*        game.batch.begin();
        game.font.draw(game.batch, diceRollText, 0, SCREEN_HEIGHT);
        game.font.draw(game.batch, "" + cheatsAvailable, SCREEN_WIDTH - 100, 150);
        game.batch.end();*/

    }

    public int[] getRollResults() {
        return rollResults;
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
        // dispose all textures
        for (int i = 0; i < dieTextures.length; i++) {
            dieTextures[i].dispose();
        }

        stage.dispose();
    }

}
