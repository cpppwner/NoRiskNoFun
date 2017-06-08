package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import gmbh.norisknofun.assets.AssetFactory;
import gmbh.norisknofun.assets.AssetFont;
import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.assets.AssetTexture;
import gmbh.norisknofun.scene.Assets;

/**
 * Text button scene object.
 *
 * <p>
 *     In contrary to the {@link ImageButtonSceneObject} this class has text inside.
 *     If the button's text might change, prefer this scene object over the {@link ImageButtonSceneObject}.
 * </p>
 */
public class TextButtonSceneObject extends ButtonSceneObject {

    /**
     * Default font size in pixels.
     */
    private static final int DEFAULT_FONT_SIZE = 36;

    /**
     * Font asset.
     */
    private final AssetFont font;
    /**
     * Texture for button borders.
     */
    private final AssetTexture texture;
    /**
     * Wrapped ligdx {@link TextButton} object.
     */
    private final TextButton textButton;

    /**
     * Create text button scene object.
     *
     * @param assetFactory Factory for loading assets.
     * @param buttonText Button's text to display.
     */
    public TextButtonSceneObject(AssetFactory assetFactory, String buttonText) {
        this(assetFactory, buttonText, null);
    }

    /**
     * Create text button scene object.
     *
     * @param assetFactory Factory for loading assets.
     * @param buttonText Button's text to display.
     * @param sound The sound to play if clicked or null to not play any sound.
     */
    public TextButtonSceneObject(AssetFactory assetFactory, String buttonText, AssetSound sound) {

        super(sound);

        font = assetFactory.createAssetFont(DEFAULT_FONT_SIZE, Color.BLACK, 1.0f);
        texture = assetFactory.createAssetTexture(Assets.TEXT_BUTTON_FILENAME);
        textButton = new TextButton(buttonText, createDefaultButtonStyle());

        super.setBounds(textButton.getX(), textButton.getY(), textButton.getWidth(), textButton.getHeight());
    }

    /**
     * Create default text button style.
     * @return
     */
    private TextButton.TextButtonStyle createDefaultButtonStyle() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font.toBitmapFont();
        style.up = texture.createDrawable();
        style.down = texture.createDrawable();
        style.fontColor = new Color(0f, 0f, 0f, 1);
        style.downFontColor = new Color(0, 0.4f, 0, 1);

        return style;
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
        texture.dispose();
        font.dispose();
        super.dispose();
    }
}
