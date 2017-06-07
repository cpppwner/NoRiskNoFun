package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import gmbh.norisknofun.assets.AssetFactory;
import gmbh.norisknofun.assets.AssetFont;
import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.assets.AssetTexture;
import gmbh.norisknofun.scene.Assets;
import gmbh.norisknofun.scene.SceneObject;

/**
 * Text button scene object.
 */
public class TextButtonSceneObject extends ButtonSceneObject {

    private static final int DEFAULT_FONT_SIZE = 36;

    private final AssetFont font;
    private final AssetTexture texture;
    private final TextButton textButton;

    public TextButtonSceneObject(AssetFactory assetFactory, String buttonText, AssetSound sound) {

        super(sound);

        font = assetFactory.createAssetFont(DEFAULT_FONT_SIZE, Color.BLACK, 1.0f);
        texture = assetFactory.createAssetTexture(Assets.TEXT_BUTTON_FILENAME);
        textButton = new TextButton(buttonText, createDefaultButtonStyle());
    }

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
        font.dispose();
        texture.dispose();
        super.dispose();
    }
}
