package gmbh.norisknofun.assets.impl.label;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import gmbh.norisknofun.assets.AssetLabel;

/**
 * Default implementation of {@link AssetLabel}.
 */
public class AssetLabelImpl implements AssetLabel {

    private static final String DEFAULT_FONT_FILENAME = "fonts/DroidSansMono.ttf";

    private final Label label;
    private final BitmapFont labelFont;

    public AssetLabelImpl(String text, int fontSize, Color color, float borderWidth) {

        labelFont = createLabelFont(fontSize, color, borderWidth);
        label = new Label(text, new Label.LabelStyle(this.labelFont, color));
    }

    private static BitmapFont createLabelFont(int fontSize, Color color, float borderWidth) {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(DEFAULT_FONT_FILENAME));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = color;
        parameter.size = fontSize;
        parameter.borderWidth = borderWidth;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        return font;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        label.draw(batch, parentAlpha);
    }

    @Override
    public float getWidth() {

        return label.getWidth();
    }

    @Override
    public float getHeight() {

        return label.getHeight();
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {

        label.setBounds(x, y, width, height);
    }

    @Override
    public void setText(String text) {

        label.setText(text);
    }

    @Override
    public void dispose() {

        labelFont.dispose();
    }

    @Override
    public String getName() {

        return label.getText().toString();
    }
}
