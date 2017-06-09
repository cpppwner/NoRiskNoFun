package gmbh.norisknofun.assets;

import com.badlogic.gdx.graphics.Color;

/**
 * Container class abstracting some parameters for creating text fields.
 *
 * <p>
 *     This class is implemented using a fluent builder pattern, since there
 *     are too many things to set and a descriptor is intended to be immutable.
 * </p>
 */
public final class TextFieldDescriptor {

    private final FontDescriptor font;
    private final Color fontColor;
    private final FontDescriptor messageFont;
    private final Color messageFontColor;
    private final String hintText;

    public TextFieldDescriptor(Builder builder) {

        font = builder.font;
        fontColor = builder.fontColor;
        messageFont = builder.messageFont;
        messageFontColor = builder.messageFontColor;
        hintText = builder.hintText;
    }

    public FontDescriptor getFont() {
        return font;
    }

    public Color getFontColor() {
        return new Color(fontColor);
    }

    public FontDescriptor getMessageFont() {
        return messageFont;
    }

    public Color getMessageFontColor() {
        return new Color(messageFontColor);
    }

    public String getHintText() {
        return hintText;
    }

    @Override
    public int hashCode() {

        int result = font.hashCode();
        result += 31 * result + fontColor.hashCode();
        result += 31 * result + messageFont.hashCode();
        result += 31 * result + messageFontColor.hashCode();
        result += 31 * result + hintText.hashCode();

        return result;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o)
            return true;

        if (o == null || !(o instanceof TextFieldDescriptor))
            return false;

        TextFieldDescriptor other = (TextFieldDescriptor)o;

        return font.equals(other.font)
                && fontColor.equals(other.fontColor)
                && messageFont.equals(other.messageFont)
                && messageFontColor.equals(other.messageFontColor)
                && hintText.equals(other.hintText);
    }

    public static final class Builder {

        private FontDescriptor font;
        private Color fontColor = Color.BLACK;
        private FontDescriptor messageFont;
        private Color messageFontColor = Color.LIGHT_GRAY;
        private String hintText = "";

        public Builder setFont(FontDescriptor font) {
            this.font = font;
            return this;
        }

        public Builder setFontColor(Color fontColor) {
            this.fontColor = fontColor;
            return this;
        }

        public Builder setMessageFont(FontDescriptor messageFont) {
            this.messageFont = messageFont;
            return this;
        }

        public Builder setMessageFontColor(Color messageFontColor) {
            this.messageFontColor = messageFontColor;
            return this;
        }

        public Builder setHintText(String hintText) {
            this.hintText = hintText;
            return this;
        }

        public TextFieldDescriptor build() {
            checkConsistency();
            return new TextFieldDescriptor(this);
        }

        private void checkConsistency() {

            if (font == null)
                throw new IllegalStateException("font is null");
            if (fontColor == null) {
                throw new IllegalStateException("fontColor is null");
            }
            if (messageFont == null) {
                throw new IllegalStateException("messageFont is null");
            }
            if (messageFontColor == null) {
                throw new IllegalStateException("messageFontColor is null");
            }
            if (hintText == null) {
                throw new IllegalStateException("hint text is null");
            }
        }
    }
}
