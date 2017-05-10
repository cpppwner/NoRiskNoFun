package gmbh.norisknofun.scene.game.dice;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import gmbh.norisknofun.scene.SceneObject;


public class DiceSceneObject extends SceneObject {

    private Image dieImage;
    private float x;
    private float y;
    private float width;
    private float height;

    private int index;
    private Texture[] dieTextures;
    private static final int MAX_DIE_NUMBER = 6;

    public DiceSceneObject(int dieNumber, int index, float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.index = index;

        initializeDieTextures();

        createDie(dieNumber);
        setBounds(x, y, width, height);
    }

    private void initializeDieTextures() {
        dieTextures = new Texture[MAX_DIE_NUMBER];

        for (int i = 0; i < MAX_DIE_NUMBER; i++) {
            dieTextures[i] = new Texture(Gdx.files.internal("dice/dieWhite"+(i+1)+".png"));
        }
    }

    private void createDie(int dieNumber) {
        dieImage = new Image(dieTextures[dieNumber-1]);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        dieImage.draw(batch, parentAlpha);
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x,y,width, height);
        dieImage.setBounds(x,y,width,height);

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Image getDie() {
        return dieImage;
    }

    /**
     * Set the number on the die image
     * @param number 1-6
     */
    public void setDieNumber(int number) {
        if (number < 1 || number > MAX_DIE_NUMBER) {
            return;
        }

        dieImage = new Image(dieTextures[number-1]);
        dieImage.setBounds(x,y,width,height);
    }

    /**
     * Get index of the die within the dieObjects list in DiceRollScene
     * Used to set the ClickListener
     * @return index of the die
     */
    public int getIndex() {
        return index;
    }
}
