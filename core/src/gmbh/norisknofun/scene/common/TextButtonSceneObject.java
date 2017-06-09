package gmbh.norisknofun.scene.common;

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

        setSize(textButton.getWidth(), textButton.getHeight());
        addActor(textButton.getActor());
    }

    @Override
    public void setBounds(float x, float y, float width, float height){

        textButton.setBounds(0.0f, 0.0f, width, height);
        super.setBounds(x, y, width, height);
    }

    @Override
    public void dispose() {
        textButton.dispose();
        super.dispose();
    }
}
