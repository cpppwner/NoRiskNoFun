package gmbh.norisknofun.assets.impl.font;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import gmbh.norisknofun.assets.FontDescriptor;

/**
 * Class used to generate libgdx {@link BitmapFont} out of our own {@link FontDescriptor}.
 */
public class FontGenerator {

    /**
     * Font descriptor.
     */
    private final FontDescriptor fontDescriptor;


    public FontGenerator(FontDescriptor fontDescriptor) {

        this.fontDescriptor = fontDescriptor;
    }

    /**
     * Generate the bitmap font.
     */
    public BitmapFont generateFont() {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontDescriptor.getFontFilename()));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = fontDescriptor.getForegroundColor();
        parameter.size = fontDescriptor.getFontSize();
        parameter.borderWidth = fontDescriptor.getBorderWidth();
        parameter.borderColor = fontDescriptor.getBorderColor();
        parameter.borderStraight = fontDescriptor.isStraightBorderUsed();
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        return font;
    }
}
