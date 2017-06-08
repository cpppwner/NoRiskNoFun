package gmbh.norisknofun.assets;

import com.badlogic.gdx.graphics.Color;

/**
 * Container class abstracting some parameters for creating {@link AssetTextButton}.
 *
 * <p>
 *     This class is implemented using a fluent builder pattern, since there
 *     are too many things to set and a descriptor is intended to be immutable.
 * </p>
 */
public final class TextButtonDescriptor {

    private final FontDescriptor textButtonFont;
    private final String upTextureFilename;
    private final String downTextureFilename;
    private final Color fontColor;
    private final Color downFontColor;

    private TextButtonDescriptor(Builder builder) {
        textButtonFont = builder.textButtonFont;
        upTextureFilename = builder.upTextureFilename;
        downTextureFilename = builder.downTextureFilename;
        fontColor = builder.fontColor;
        downFontColor = builder.downFontColor;
    }

    public FontDescriptor getTextButtonFont() {
        return textButtonFont;
    }

    public String getUpTextureFilename() {
        return upTextureFilename;
    }

    public String getDownTextureFilename() {
        return downTextureFilename;
    }

    public Color getFontColor() {
        return new Color(fontColor);
    }

    public Color getDownFontColor() {
        return new Color(downFontColor);
    }

    @Override
    public int hashCode() {

        int result = textButtonFont.hashCode();
        result = 31 * result + upTextureFilename.hashCode();
        result = 31 * result + downTextureFilename.hashCode();
        result = 31 * result + fontColor.hashCode();
        result = 31 * result + downFontColor.hashCode();

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true; // instance equality

        if (o == null || !(o instanceof TextButtonDescriptor)) {
            return false;
        }

        TextButtonDescriptor other = (TextButtonDescriptor)o;

        return textButtonFont.equals(other.textButtonFont)
                && upTextureFilename.equals(other.upTextureFilename)
                && downTextureFilename.equals(other.downTextureFilename)
                && fontColor.equals(other.fontColor)
                && downFontColor.equals(other.downFontColor);
    }

    public static final class Builder {

        private FontDescriptor textButtonFont;
        private String upTextureFilename;
        private String downTextureFilename;
        private Color fontColor = Color.BLACK;
        private Color downFontColor = Color.BLACK;

        public Builder setTextButtonFont(FontDescriptor textButtonFont) {
            this.textButtonFont = textButtonFont;
            return this;
        }

        public Builder setUpTextureFilename(String upTextureFilename) {
            this.upTextureFilename = upTextureFilename;
            return this;
        }

        public Builder setDownTextureFilename(String downTextureFilename) {
            this.downTextureFilename = downTextureFilename;
            return this;
        }

        public Builder setFontColor(Color fontColor) {
            this.fontColor = fontColor;
            return this;
        }

        public Builder setDownFontColor(Color downFontColor) {
            this.downFontColor = downFontColor;
            return this;
        }

        public TextButtonDescriptor build() {
            checkConsistency();
            return new TextButtonDescriptor(this);
        }

        private void checkConsistency() {

            if (textButtonFont == null) {
                throw new IllegalStateException("textButtonFont is null");
            }
            if (upTextureFilename == null || upTextureFilename.isEmpty()) {
                throw new IllegalStateException("upTextureFilename is null or empty");
            }
            if (downTextureFilename == null || downTextureFilename.isEmpty()) {
                throw new IllegalStateException("downTextureFilename is null or empty");
            }
            if (fontColor == null) {
                throw new IllegalStateException("fontColor is null");
            }
            if (downFontColor == null) {
                throw new IllegalStateException("downFontColor is null");
            }
        }
    }
}
