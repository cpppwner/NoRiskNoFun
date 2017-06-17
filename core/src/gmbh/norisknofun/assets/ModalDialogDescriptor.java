package gmbh.norisknofun.assets;

import com.badlogic.gdx.graphics.Color;

/**
 * Container class abstracting some parameters for creating a modal dialog asset.
 *
 * <p>
 *     This class is implemented using a fluent builder pattern, since there
 *     are too many things to set and a descriptor is intended to be immutable.
 * </p>
 */
public final class ModalDialogDescriptor {

    /** Font descriptor describing title font used by the modal dialog. */
    private final FontDescriptor titleFont;
    private final Color titleFontColor;
    private final String title;
    private final FontDescriptor messageFont;
    private final Color backgroundColor;
    private final TextButtonDescriptor buttonDescriptor;

    private ModalDialogDescriptor(Builder builder) {

        titleFont = builder.titleFont;
        titleFontColor = builder.titleFontColor;
        title = builder.title;
        messageFont = builder.messageFont;
        backgroundColor = builder.backgroundColor;
        buttonDescriptor = builder.buttonDescriptor;
    }

    public FontDescriptor getTitleFont() {
        return titleFont;
    }

    public Color getTitleFontColor() {
        return new Color(titleFontColor);
    }

    public String getTitle() {
        return title;
    }

    public FontDescriptor getMessageFont() {
        return messageFont;
    }

    public Color getBackgroundColor() {
        return new Color(backgroundColor);
    }

    public TextButtonDescriptor getTextButtonDescriptor() {
        return buttonDescriptor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        ModalDialogDescriptor other = (ModalDialogDescriptor) o;

        if (!titleFont.equals(other.titleFont))
            return false;
        if (!titleFontColor.equals(other.titleFontColor))
            return false;
        if (!title.equals(other.title))
            return false;
        if (!messageFont.equals(other.messageFont))
            return false;
        if (!buttonDescriptor.equals(other.buttonDescriptor))
            return false;
        return backgroundColor.equals(other.backgroundColor);
    }

    @Override
    public int hashCode() {

        int result = titleFont.hashCode();
        result = 31 * result + titleFontColor.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + messageFont.hashCode();
        result = 31 * result + buttonDescriptor.hashCode();
        result = 31 * result + backgroundColor.hashCode();

        return result;
    }

    public static final class Builder {

        private FontDescriptor titleFont;
        private Color titleFontColor = Color.BLACK;
        private String title = "";
        private FontDescriptor messageFont;
        private Color backgroundColor = Color.LIGHT_GRAY;
        private TextButtonDescriptor buttonDescriptor;

        public Builder setTitleFont(FontDescriptor titleFont) {
            this.titleFont = titleFont;
            return this;
        }

        public Builder setTitleFontColor(Color titleFontColor) {
            this.titleFontColor = titleFontColor;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMessageFont(FontDescriptor messageFont) {
            this.messageFont = messageFont;
            return this;
        }

        public Builder setBackgroundColor(Color backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder setButtonDescriptor(TextButtonDescriptor buttonDescriptor) {
            this.buttonDescriptor = buttonDescriptor;
            return this;
        }

        public ModalDialogDescriptor build() {
            checkConsistency();
            return new ModalDialogDescriptor(this);
        }

        private void checkConsistency() {

            if (titleFont == null) {
                throw new IllegalStateException("font is null");
            }
            if (titleFontColor == null) {
                throw new IllegalStateException("fontColor is null");
            }
            if (title == null) {
                throw new IllegalStateException("title is null");
            }
            if (messageFont == null) {
                throw new IllegalStateException("messageFont is null");
            }
            if (backgroundColor == null) {
                throw new IllegalStateException("backgroundColor is null");
            }
            if (buttonDescriptor == null) {
                throw new IllegalStateException("buttonDescriptor is null");
            }
        }
    }
}
