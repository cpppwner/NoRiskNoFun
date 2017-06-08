package gmbh.norisknofun.assets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Disposable;

/**
 * Image button asset, wrapping libgdx {@link ImageButton}.
 */
public interface AssetImageButton extends Asset, Disposable {

    /**
     * Get x-coordinate of wrapped {@link ImageButton}.
     */
    float getX();

    /**
     * Get y-coordinate of wrapped {@link ImageButton}.
     */
    float getY();

    /**
     * Get width of wrapped {@link ImageButton}.
     */
    float getWidth();

    /**
     * Get height of wrapped {@link ImageButton}.
     */
    float getHeight();

    /**
     * Set bounds of wrapped {@link ImageButton}.
     *
     * @param x x-coordinate starting from left corner in pixel
     * @param y y-coordinate starting from lower corner in pixel
     * @param width width in pixel
     * @param height height in pixel
     */
    void setBounds(float x, float y, float width, float height);

    /**
     * Draw image button.
     *
     * @param batch Batch object used for drawing.
     * @param parentAlpha Alpha value of parent.
     */
    void draw(Batch batch, float parentAlpha);
}
