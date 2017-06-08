package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.assets.AssetTexture;

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
     * The texture used as image button.
     */
    private final AssetTexture texture;
    /**
     * Wrapped libgdx {@link ImageButton}.
     */
    private final ImageButton imageButton;

    /**
     * Initialize image button with given texture.
     *
     * @param texture The texture used to represent this image button.
     *                The texture is disposed by this scene object.
     */
    public ImageButtonSceneObject(AssetTexture texture) {
        this(texture, null);
    }

    /**
     * Initialize image button with texture and sound to play on button click.
     *
     * @param texture The texture used to display.
     *                The texture is disposed by this scene object.
     * @param sound Sound played when the button is pressed.
     *              Note: The caller of of this constructor is responsible for disposing the sound,
     *              but only when the button instance is no longer used.
     */
    public ImageButtonSceneObject(AssetTexture texture, AssetSound sound) {
        super(sound);

        this.texture = texture;
        this.imageButton = new ImageButton(texture.createDrawable());

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
        texture.dispose();
        super.dispose();
    }
}
