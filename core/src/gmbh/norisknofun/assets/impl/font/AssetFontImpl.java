package gmbh.norisknofun.assets.impl.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import gmbh.norisknofun.assets.AssetFont;

/**
 * Default implementation of {@link AssetFont}.
 */
public class AssetFontImpl implements AssetFont {

    /**
     * Font's filename.
     */
    private static final String DEFAULT_FONT_FILENAME = "fonts/DroidSansMono.ttf";

    /**
     * Underlying libgdx font class.
     */
    private final BitmapFont font;

    public AssetFontImpl(int fontSize, Color color, float borderWidth) {
        font = generateFont(fontSize, color, borderWidth);
    }

    /**
     * Generate the bitmap font.
     */
    private static BitmapFont generateFont(int fontSize, Color color, float borderWidth) {

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
    public BitmapFont toBitmapFont() {
        return font;
    }

    @Override
    public void dispose() {
        font.dispose();
    }

    @Override
    public String getName() {
        return DEFAULT_FONT_FILENAME;
    }
}
