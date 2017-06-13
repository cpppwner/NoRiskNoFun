package gmbh.norisknofun.assets.impl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import gmbh.norisknofun.assets.AssetTextButton;
import gmbh.norisknofun.assets.TextButtonDescriptor;

/**
 * Default implementation of {@link AssetTextButton}.
 */
class AssetTextButtonImpl implements AssetTextButton {

    /**
     * Low level gdx asset cache.
     */
    private final LibGdxAssetCache cache;

    /**
     * Descriptor object describing the text button's style.
     */
    private final TextButtonDescriptor textButtonDescriptor;


    private final BitmapFont font;
    private final Texture upTexture;
    private final Texture downTexture;
    private final TextButton textButton;

    /**
     * Create AssetTextButton.
     * @param cache Low level asset cache.
     * @param initialButtonText The initial button text.
     * @param textButtonDescriptor Descriptor object describing some button text properties.
     */
    AssetTextButtonImpl(LibGdxAssetCache cache, String initialButtonText, TextButtonDescriptor textButtonDescriptor) {

        this.cache = cache;
        this.textButtonDescriptor = textButtonDescriptor;
        font = cache.getFont(textButtonDescriptor.getTextButtonFont());
        upTexture = cache.getTexture(textButtonDescriptor.getUpTextureFilename());
        downTexture = cache.getTexture(textButtonDescriptor.getDownTextureFilename());

        textButton = new TextButton(initialButtonText, createButtonStyle());
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
    public String getName() {

        return textButton.getName();
    }

    @Override
    public void setDisabled() {
        textButton.setDisabled(true);
    }

    @Override
    public void setEnabled() {
        textButton.setDisabled(false);
    }

    @Override
    public boolean isEnabled() {
        return !textButton.isDisabled();
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
    public Actor getActor() {
        return textButton;
    }

    @Override
    public void dispose() {

        cache.releaseFont(font);
        cache.releaseTexture(upTexture);
        cache.releaseTexture(downTexture);
    }

    /**
     * Get libgdx {@link TextButton} wrapped by this object.
     */
    TextButton getTextButton() {

        return textButton;
    }
}
