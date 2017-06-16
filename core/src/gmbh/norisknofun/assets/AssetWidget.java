package gmbh.norisknofun.assets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

/**
 * User interface asset (widget asset).
 */
interface AssetWidget {

    /**
     * Get x-coordinate of wrapped {@link ImageButton}.
     *
     * @return x-coordinate of widget's current position.
     */
    float getX();

    /**
     * Get y-coordinate of wrapped {@link ImageButton}.
     *
     * @return y-coordinate of widget's current position.
     */
    float getY();

    /**
     * Get width of wrapped {@link ImageButton}.
     *
     * @return width of widget
     */
    float getWidth();

    /**
     * Get height of wrapped {@link ImageButton}.
     *
     * @return height of widget
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
     * Get wrapped actor.
     *
     * @return The underlying libgdx {@link Actor}.
     */
    Actor getActor();
}
