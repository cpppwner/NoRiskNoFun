package gmbh.norisknofun.assets;

import com.badlogic.gdx.graphics.Color;

/**
 * Container class abstracting some font parameters for font creation.
 *
 * <p>
 *     This class is implemented using a fluent builder pattern, since there
 *     are too many things to set and a descriptor is intended to be immutable.
 * </p>
 */
public final class FontDescriptor {

    /** Filename of the font */
    private final String fontFilename;
    /** Font size in pixels - default 16px */
    private final int fontSize;
    /** Foreground color */
    private final Color foregroundColor;
    /** Border width in pixel */
    private final float borderWidth;
    /** Border color - only applied if width != 0 */
    private final Color borderColor;
    /** Flat indicating whether to use straight (mitered) borders ({@code true}), rounded otherwise ({@code false}) */
    private final boolean borderStraight;

    /**
     * Initialize the FontDescriptor using the Builder.
     */
    private FontDescriptor(FontDescriptor.Builder builder) {
        fontFilename = builder.fontFilename;
        fontSize = builder.fontSize;
        foregroundColor = builder.foregroundColor;
        borderWidth = builder.borderWidth;
        borderColor = builder.borderColor;
        borderStraight = builder.borderStraight;
    }

    /**
     * Get font filename.
     *
     * @return Font filename, relative to assets directory.
     */
    public String getFontFilename() {
        return fontFilename;
    }

    /**
     * Get font size in pixels.
     *
     * @return Font size in pixels.
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * Get foreground color.
     *
     * <p>
     *     A new {@link Color} instance is returned, since this class is immutable and libgdx Color is not.
     * </p>
     *
     * @return Foreground color of the font.
     */
    public Color getForegroundColor() {
        return new Color(foregroundColor);
    }

    /**
     * Get border width in pixels.
     *
     * @return Font's border width in pixels.
     */
    public float getBorderWidth() {
        return borderWidth;
    }

    /**
     * Get border color.
     *
     * <p>
     *     A new {@link Color} instance is returned, since this class is immutable and libgdx Color is not.
     * </p>
     *
     * @return Font's border color.
     */
    public Color getBorderColor() {
        return new Color(borderColor);
    }

    /**
     * Get a boolean flag indicating whether straight (mitered) are used ({@code true}) or rounded ({@code false}).
     *
     * @return {@code true} if straight borders shall be used, {@code false} otherwise.
     */
    public boolean isStraightBorderUsed() {
        return borderStraight;
    }

    @Override
    public int hashCode() {
        int result = fontFilename.hashCode();
        result = 31 * result + fontSize;
        result = 31 * result + foregroundColor.hashCode();
        result = 31 * result + Float.floatToIntBits(borderWidth);
        result = 31 * result + borderColor.hashCode();
        result = 31 * result + (borderStraight ? 1 : 0);

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true; // reference equality
        if ((o == null)  || !(o instanceof FontDescriptor))
            return false;

        FontDescriptor other = (FontDescriptor)o;
        return compareFontParameters(other) && compareBorderParameters(other);
    }

    /**
     * Compare method to compare font related settings.
     *
     * @param other The other {@link FontDescriptor} to compare to.
     * @return {@code true} if font related settings are equal, {@code false} otherwise.
     */
    private boolean compareFontParameters(FontDescriptor other) {

        return fontFilename.equals(other.fontFilename)
                && fontSize == other.fontSize
                && foregroundColor.equals(other.foregroundColor);
    }

    /**
     * Compare method to compare border related settings.
     *
     * @param other The other {@link FontDescriptor} to compare to.
     * @return {@code true} if font border settings are equal, {@code false} otherwise.
     */
    private boolean compareBorderParameters(FontDescriptor other) {

        return Float.compare(borderWidth, other.borderWidth) == 0
                && borderColor.equals(other.borderColor)
                && borderStraight == other.borderStraight;
    }

    /**
     * Fluent builder used to build the {@link FontDescriptor}.
     */
    public static final class Builder {

        /** Filename of the font */
        private String fontFilename = null;
        /** Font size in pixels - default 16px */
        private int fontSize = 36;
        /** Foreground color */
        private Color foregroundColor = Color.WHITE;
        /** Border width in pixel */
        private float borderWidth = 0;
        /** Border color - only applied if width != 0 */
        private Color borderColor = Color.BLACK;
        /** Flat indicating whether to use straight (mitered) borders ({@code true}), rounded otherwise ({@code false}) */
        private boolean borderStraight = false;

        /**
         * Set font filename.
         *
         * @param fontFilename filename
         * @return {@code this}
         *
         */
        public Builder setFontFilename(String fontFilename) {
            this.fontFilename = fontFilename;
            return this;
        }

        /**
         * Set font size in pixels.
         *
         * @param fontSize fontsize in pixels
         * @return {@code this}
         */
        public Builder setFontSize(int fontSize) {
            this.fontSize = fontSize;
            return this;
        }

        /**
         * Set foreground color.
         * @param foregroundColor  foreground color
         * @return {@code this}
         */
        public Builder setForegroundColor(Color foregroundColor) {
            this.foregroundColor = foregroundColor;
            return this;
        }

        /**
         * Set border width in pixels.
         *
         * @param borderWidth border width in pixels
         * @return {@code this}
         */
        public Builder setBorderWidth(float borderWidth) {
            this.borderWidth = borderWidth;
            return this;
        }

        /**
         * Set border color.
         *
         * @param borderColor Color
         * @return {@code this}
         */
        public Builder setBorderColor(Color borderColor) {
            this.borderColor = borderColor;
            return this;
        }

        /**
         * Enable using of straight (mitered) borders.
         *
         * @return {@code this}
         */
        public Builder setStraigtBorder() {
            this.borderStraight = true;
            return this;
        }

        /**
         * Enable using round borders.
         *
         * @return {@code this}
         */
        public Builder setRoundBorder() {
            this.borderStraight = false;
            return this;
        }

        /**
         * Build the {@link FontDescriptor}.
         *
         * <p>
         *     If mandatory settings were not provided or specified wrongly, and
         *     {@link IllegalStateException} is thrown.
         * </p>
         *
         * @return Built font descriptor.
         */
        public FontDescriptor build() {
            consistencyCheck();
            return new FontDescriptor(this);
        }

        /**
         * Checks provided settings for consistency and throws {@link IllegalStateException}
         * if something is missing or wrong.
         */
        private void consistencyCheck() {

            if (fontFilename == null || fontFilename.isEmpty()) {
                throw new IllegalStateException("fontFilename is null or empty");
            }
            if (fontSize <= 0) {
                throw new IllegalStateException("fontSize is less than or equal to 0");
            }
            if (borderWidth < 0) {
                throw new IllegalStateException("borderWidth is less than 0");
            }
            if (foregroundColor == null) {
                throw new IllegalStateException("foregroundColor is null");
            }
            if (borderColor == null) {
                throw new IllegalStateException("borderColor is null");
            }
        }
    }
}
