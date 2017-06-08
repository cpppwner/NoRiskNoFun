package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import gmbh.norisknofun.assets.AssetImageButton;
import gmbh.norisknofun.assets.AssetSound;

/**
 * Image button scene object.
 *
 * <p>
 *     An image button is an image acting like a button (pressable and such).
 *     This objects basically wraps the underlying libgdx {@link ImageButton}.
 * </p>
 */
public class ImageButtonSceneObject extends ButtonSceneObject {

    /**
     * Image button asset.
     */
    private final AssetImageButton imageButton;

    /**
     * Initialize image button scene object with given image button asset
     *
     * @param imageButton The image button asset.
     *                The image button is disposed by this scene object.
     */
    public ImageButtonSceneObject(AssetImageButton imageButton) {
        this(imageButton, null);
    }

    /**
     * Initialize image button with texture and sound to play on button click.
     *
     * @param imageButton The image button asset.
     *                The image button is disposed by this scene object.
     * @param sound Sound played when the button is pressed.
     *              Note: The caller of of this constructor is responsible for disposing the sound,
     *              but only when the button instance is no longer used.
     */
    public ImageButtonSceneObject(AssetImageButton imageButton, AssetSound sound) {
        super(sound);

        this.imageButton = imageButton;

        super.setBounds(imageButton.getX(), imageButton.getY(), imageButton.getWidth(), imageButton.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
       imageButton.draw(batch,parentAlpha);

    }

    @Override
    public void setBounds(float x, float y, float width, float height){
        super.setBounds(x,y,width,height);
        imageButton.setBounds(x,y,width,height);
    }

    @Override
    public void dispose() {
        imageButton.dispose();
        super.dispose();
    }
}
