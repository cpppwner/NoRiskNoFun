package gmbh.norisknofun.assets.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
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

    private final TextFieldDescriptor textFieldDescriptor;
    private final BitmapFont textFont;
    private final BitmapFont hintFont;
    private final Pixmap backgroundPixmap;
    private final Texture backgroundTexture;
    private final TextField textField;

    AssetTextFieldImpl(String initialFieldText, TextFieldDescriptor textFieldDescriptor) {

        this.textFieldDescriptor = textFieldDescriptor;
        textFont = new FontGenerator(textFieldDescriptor.getFont()).generateFont();
        hintFont = new FontGenerator(textFieldDescriptor.getMessageFont()).generateFont();
        backgroundPixmap = createDefaultBackgroundPixmap();
        backgroundTexture = new Texture(backgroundPixmap);
        textField = new TextField(initialFieldText, createTextFieldStyle());
        textField.setMessageText(textFieldDescriptor.getHintText());
    }

    private static Pixmap createDefaultBackgroundPixmap() {

        Pixmap backgroundPixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        backgroundPixmap.setColor(Color.WHITE);
        backgroundPixmap.fill();

        return backgroundPixmap;
    }

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

        textFont.dispose();
        hintFont.dispose();
        backgroundTexture.dispose();
        backgroundPixmap.dispose();
    }
}
