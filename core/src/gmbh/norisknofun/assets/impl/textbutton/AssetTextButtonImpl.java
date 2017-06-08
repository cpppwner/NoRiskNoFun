package gmbh.norisknofun.assets.impl.textbutton;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import gmbh.norisknofun.assets.AssetTextButton;
import gmbh.norisknofun.assets.TextButtonDescriptor;
import gmbh.norisknofun.assets.impl.font.FontGenerator;

/**
 * Created by cpppwner on 08.06.17.
 */

public class AssetTextButtonImpl implements AssetTextButton {

    private final TextButtonDescriptor textButtonDescriptor;
    private final BitmapFont font;
    private final Texture upTexture;
    private final Texture downTexture;
    private final TextButton textButton;

    public AssetTextButtonImpl(String initialButtonText, TextButtonDescriptor textButtonDescriptor) {
        this.textButtonDescriptor = textButtonDescriptor;
        font = new FontGenerator(textButtonDescriptor.getTextButtonFont()).generateFont();
        upTexture = new Texture(textButtonDescriptor.getUpTextureFilename());
        downTexture = (textButtonDescriptor.getUpTextureFilename().equals(textButtonDescriptor.getDownTextureFilename()))
            ? upTexture
            : new Texture(textButtonDescriptor.getDownTextureFilename());
        textButton = new TextButton(initialButtonText, createButtonStyle());
    }

    @Override
    public void dispose() {

        font.dispose();
        upTexture.dispose();
        if (!textButtonDescriptor.getUpTextureFilename().equals(textButtonDescriptor.getDownTextureFilename())) {
            downTexture.dispose();
        }
    }

    @Override
    public String getName() {

        return textButton.getName();
    }

    /**
     * Create default text button style.
     */
    private TextButton.TextButtonStyle createButtonStyle() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = font;
        style.up = new TextureRegionDrawable(new TextureRegion(upTexture));
        style.down = new TextureRegionDrawable(new TextureRegion(downTexture));
        style.fontColor = textButtonDescriptor.getFontColor();
        style.downFontColor = textButtonDescriptor.getDownFontColor();

        return style;
    }

    @Override
    public float getX() {

        return textButton.getX();
    }

    @Override
    public float getY() {

        return textButton.getY();
    }

    @Override
    public float getWidth() {

        return textButton.getWidth();
    }

    @Override
    public float getHeight() {

        return textButton.getHeight();
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {

        textButton.setBounds(x, y, width, height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        textButton.draw(batch, parentAlpha);
    }
}
