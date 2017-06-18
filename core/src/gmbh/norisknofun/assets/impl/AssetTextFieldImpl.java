package gmbh.norisknofun.assets.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import gmbh.norisknofun.assets.AssetTextField;
import gmbh.norisknofun.assets.TextFieldDescriptor;

/**
 * Default implementation of {@link AssetTextField}.
 */
class AssetTextFieldImpl implements AssetTextField {

    /**
     * Low level gdx asset cache.
     */
    private final LibGdxAssetCache cache;
    /**
     * Descriptor object describing the style of this text field.
     */
    private final TextFieldDescriptor textFieldDescriptor;
    /**
     * Font used to render the text.
     */
    private final BitmapFont textFont;
    /**
     * Font used to render the hint text.
     */
    private final BitmapFont hintFont;
    /**
     * Background pixmap texture.
     */
    private final Texture backgroundTexture;
    /**
     * Wrapped text field.
     */
    private final TextField textField;

    AssetTextFieldImpl(LibGdxAssetCache cache, String initialFieldText, TextFieldDescriptor textFieldDescriptor) {

        this.cache = cache;
        this.textFieldDescriptor = textFieldDescriptor;
        textFont = cache.getFont(textFieldDescriptor.getFont());
        hintFont = cache.getFont(textFieldDescriptor.getMessageFont());
        backgroundTexture = cache.getPixMapTexture(Color.WHITE);
        textField = new TextField(initialFieldText, createTextFieldStyle());
        textField.setMessageText(textFieldDescriptor.getHintText());
    }

    /**
     * Create text style.
     */
    private TextField.TextFieldStyle createTextFieldStyle() {

        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = textFont;
        style.messageFont = hintFont;
        style.fontColor = textFieldDescriptor.getFontColor();
        style.messageFontColor = textFieldDescriptor.getMessageFontColor();
        style.background = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        return style;
    }


    @Override
    public String getName() {
        return textField.getName();
    }

    @Override
    public float getX() {
        return textField.getX();
    }

    @Override
    public float getY() {
        return textField.getY();
    }

    @Override
    public float getWidth() {
        return textField.getWidth();
    }

    @Override
    public float getHeight() {
        return textField.getHeight();
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        textField.setBounds(x, y, width, height);
    }

    @Override
    public String getText() {
        return textField.getText();
    }

    @Override
    public Actor getActor() {
        return textField;
    }

    @Override
    public void dispose() {

        cache.releaseFont(textFont);
        cache.releaseFont(hintFont);
        cache.releasePixMapTexture(backgroundTexture);
    }

    /**
     * Set text field filter.
     * @param textFieldFilter Filter to apply to this text field.
     */
    void setTextFieldFilter(TextField.TextFieldFilter.DigitsOnlyFilter textFieldFilter) {
        textField.setTextFieldFilter(textFieldFilter);
    }
}
