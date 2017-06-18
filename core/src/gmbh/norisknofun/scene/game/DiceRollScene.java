package gmbh.norisknofun.scene.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gmbh.norisknofun.game.GameData;
import gmbh.norisknofun.game.gamemessages.gui.EvaluateDiceResultGui;
import gmbh.norisknofun.scene.Assets;
import gmbh.norisknofun.scene.SceneBase;
import gmbh.norisknofun.scene.SceneData;
import gmbh.norisknofun.scene.SceneNames;
import gmbh.norisknofun.scene.common.LabelSceneObject;
import gmbh.norisknofun.scene.common.TextButtonSceneObject;
import gmbh.norisknofun.scene.game.dice.DiceSceneObject;

public class DiceRollScene extends SceneBase {

    private final SceneData sceneData;
    private final GameData data;

    private LabelSceneObject cheatLabel;
    private List<DiceSceneObject> dieObjects;

    private static final float GRAVITY_EARTH = 9.80665f;
    private static final float SHAKE_GRAVITY_THRESHOLD = 2.0f;

    private long lastShakeTime;
    private int[] rollResults = {0, 0, 0};
    private boolean hasBeenShaken;
    private boolean canRoll;
    private int dieAmount;


    public DiceRollScene(SceneData sceneData) {
        super(SceneNames.DICE_SCENE, Color.BLACK);
        this.sceneData = sceneData;
        this.data = sceneData.getGameData();
    }

    @Override
    public void show() {
        getStage().clear();

        dieObjects = new ArrayList<>();

        hasBeenShaken = false;
        canRoll = true;

        // spawn the correct amount of dice on the correct position
        for (int i = 0, offset = 100; i < data.getAvailableDice(); i++) {
            initDie(1, i, offset, 540);
            offset += 600;
        }

        setDiceClickListener();

        cheatLabel = initLabel();
        addSceneObject(cheatLabel);

        initBackButton();

        super.show();
    }

    /**
     * Initialize a DiceSceneObject and add it to the scene
     *
     * @param number dice image to show
     * @param index  place in the dieObjects list
     * @param x      coordinate
     * @param y      coordinate
     */
    private void initDie(int number, int index, int x, int y) {
        DiceSceneObject dieObject;
        dieObject = new DiceSceneObject(number, index, x, y, 500, 500);
        dieObjects.add(dieObject);
        addSceneObject(dieObject);
    }

    /**
     * Create a back button to return to the game scene
     */
    private void initBackButton() {
        TextButtonSceneObject backButton;
        backButton = new TextButtonSceneObject(sceneData.createTextButton("Back", Assets.DICE_CHEATS_TEXT_BUTTON_DESCRIPTOR), null);
        backButton.setBounds(1000, 100, 500, 100);
        backButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {

                if (!canRoll) {
                    writeRollResult(); // write roll result only now as we're done when we press back
                    // clear stage here as we have to redraw it on next press anyway
                    data.setDiceRoll(rollResults);
                    sceneData.sendMessageFromGui(new EvaluateDiceResultGui());
                }
            }
        });
        addSceneObject(backButton);
    }

    /**
     * Create a label
     *
     * @return created LabelSceneObject
     */
    private LabelSceneObject initLabel() {

        return new LabelSceneObject(sceneData.createLabel(Integer.toString(data.getCheatsAvailable()), Assets.FONT_36PX_WHITE_WITH_BORDER));
    }

    /**
     * Check if the device is currently being shaken
     *
     * @return true if the device registered a shake
     */
    private boolean hasShaken() {
        // get current gravity readings of accelerometer axes in relation to earth's gravity
        float xGrav = Gdx.input.getAccelerometerX() / GRAVITY_EARTH;
        float yGrav = Gdx.input.getAccelerometerY() / GRAVITY_EARTH;
        float zGrav = Gdx.input.getAccelerometerZ() / GRAVITY_EARTH;

        // gForce will be close to 1 when there is no movement.
        float gForce = (float) Math.sqrt((xGrav * xGrav) + (yGrav * yGrav) + (zGrav * zGrav));

        return (gForce > SHAKE_GRAVITY_THRESHOLD);
    }

    /**
     * Roll all dice and update their results
     */
    private void diceRoll() {
        Random rnd = new Random(TimeUtils.nanoTime());

        // generate a random number from 1-6
        for (int i = 0; i < rollResults.length; i++) {
            rollResults[i] = rnd.nextInt(6) + 1;
        }
    }

    /**
     * "Animate" the dice roll
     */
    private void randomizeDice() {
        Random rnd = new Random();
        rnd.setSeed(TimeUtils.nanoTime());

        for (int i = 0; i < dieAmount; i++) {
            dieObjects.get(i).setDieNumber(rnd.nextInt(6) + 1);
        }
    }

    /**
     * Set the images to actual roll results
     */
    private void showRollResult() {

        for (int i = 0; i < dieAmount; i++) {
            dieObjects.get(i).setDieNumber(rollResults[i]);
        }
    }

    /**
     * Set ClickListener to all die.
     * Used for cheat function.
     */
    private void setDiceClickListener() {

        for (int i = 0; i < dieObjects.size(); i++) {

            dieObjects.get(i).addListener(new DieCheatClickListener(i));
        }
    }


    /**
     * Set the amount of dice available in the scene.
     * This changes according to the Risiko rules
     *
     * @param dieAmount amount of dice available
     */
    public void setDieAmount(int dieAmount) {
        this.dieAmount = dieAmount;
    }

    /**
     * Write the roll result back to GameData after the roll is done
     */
    private void writeRollResult() {
        data.setDiceRoll(rollResults);
    }


    @Override
    public void render(float delta) {

        // set hasBeenShaken to true once hasShaken() registered, so the if statement
        // continues to the animation/result even if device isn't moving anymore
        if ((hasShaken() || hasBeenShaken) && canRoll) {


            // only update if it hasn't been shaken in the last 2 seconds
            if (TimeUtils.millis() - lastShakeTime > 5000) {
                diceRoll();
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

        super.render(delta);
    }

    /**
     * Special click listener, listening for cheats.
     */
    private final class DieCheatClickListener extends ClickListener {

        /**
         * The die index for cheating.
         */
        private final int dieIndex;

        /**
         * Initializer click listener.
         */
        private DieCheatClickListener(int dieIndex) {
            this.dieIndex = dieIndex;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            tryCheat(dieIndex);
            return true;
        }

        /**
         * Check if player can cheat and do so if it's available.
         *
         * @param index index of the die to re-roll
         */
        private void tryCheat(int index) {
            if (data.getCheatsAvailable() > 0) {
                diceRoll(index);
                dieObjects.get(index).setDieNumber(rollResults[index]);
                data.updateCheatsAvailable(-1);
                cheatLabel.setText(Integer.toString(data.getCheatsAvailable()));
            }
        }


        /**
         * Roll only a single die and update the result
         *
         * @param index die to roll
         */
        private void diceRoll(int index) {
            Random rnd = new Random(TimeUtils.nanoTime());

            rollResults[index] = rnd.nextInt(6) + 1;
        }
    }

}
