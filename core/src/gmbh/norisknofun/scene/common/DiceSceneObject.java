package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import gmbh.norisknofun.scene.SceneObject;

/**
 * Created by user on 08.05.17.
 */

public class DiceSceneObject extends SceneObject {

    private Image dieImage;
    private float x, y, width, height;
    private int index;

    public DiceSceneObject(int dieNumber, int index, float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.index = index;

        createDie(dieNumber);
        setBounds(x, y, width, height);
    }

    private void createDie(int dieNumber) {
        dieImage = new Image(new Texture(Gdx.files.internal("dice/dieWhite"+dieNumber+".png")));
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

        dieImage = new Image(new Texture(Gdx.files.internal("dice/dieWhite"+number+".png")));
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
