package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.graphics.g2d.Batch;

import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.assets.AssetTextButton;

/**
 * Text button scene object.
 *
 * <p>
 *     In contrary to the {@link ImageButtonSceneObject} this class has text inside.
 *     If the button's text might change, prefer this scene object over the {@link ImageButtonSceneObject}.
 * </p>
 */
public class TextButtonSceneObject extends ButtonSceneObject {

    private final AssetTextButton textButton;

    /**
     * Create text button scene object.
     *
     * @param textButton Wrapped text button.
     */
    public TextButtonSceneObject(AssetTextButton textButton) {
        this(textButton, null);
    }

    /**
     * Create text button scene object.
     *
     * @param sound The sound to play if clicked or null to not play any sound.
     */
    public TextButtonSceneObject(AssetTextButton textButton, AssetSound sound) {

        super(sound);
        this.textButton = textButton;

        super.setBounds(textButton.getX(), textButton.getY(), textButton.getWidth(), textButton.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        textButton.draw(batch,parentAlpha);
    }

    @Override
    public void setBounds(float x, float y, float width, float height){
        super.setBounds(x,y,width,height);
        textButton.setBounds(x,y,width,height);
    }

    @Override
    public void dispose() {
        textButton.dispose();
        super.dispose();
    }
}
