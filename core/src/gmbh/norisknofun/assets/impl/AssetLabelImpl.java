package gmbh.norisknofun.assets.impl;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import gmbh.norisknofun.assets.AssetLabel;
import gmbh.norisknofun.assets.FontDescriptor;

/**
 * Default implementation of {@link AssetLabel}.
 */
class AssetLabelImpl implements AssetLabel {

    private static Color DEFAULT_BACKGROUND_COLOR = new Color(0f, 0f, 0f, 0f);

    /**
     * Low level asset cache.
     */
    private final LibGdxAssetCache cache;

    /**
     * The bitmap font used for the label.
     */
    private final BitmapFont font;

    /**
     * gdx label.
     */
    private final Label label;

    /**
     * Current background texture - be careful - this must be released when changed.
     */
    private Texture currentBackground;

    /**
     * Create a label with given text and font descriptor.
     *
     * @param cache Low level asset cache.
     * @param text The text to show (might change).
     * @param fontDescriptor Container class describing font properties of this label.
     */
    AssetLabelImpl(LibGdxAssetCache cache, String text, FontDescriptor fontDescriptor) {

        this.cache = cache;
        font = cache.getFont(fontDescriptor);
        label = new Label(text, createDefaultLabelStyle());
    }

    private Label.LabelStyle createDefaultLabelStyle() {

        currentBackground = cache.getPixMapTexture(DEFAULT_BACKGROUND_COLOR);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, font.getColor());
        labelStyle.background = new Image(currentBackground).getDrawable();

        return labelStyle;
    }

    @Override
    public String getName() {

        return label.getText().toString();
    }

    @Override
    public float getX() {

        return label.getX();
    }

    @Override
    public float getY() {

        return label.getY();
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
    public void setTextAlignment(int alignment) {

        label.setAlignment(alignment);
    }

    @Override
    public void setBackgroundColor(Color color) {

        // prepare new style
        Texture newTexture = cache.getPixMapTexture(color);
        Label.LabelStyle newStyle = new Label.LabelStyle(label.getStyle());
        newStyle.background = new Image(newTexture).getDrawable();
        label.setStyle(newStyle);

        // release old texture & set new one
        cache.releasePixMapTexture(currentBackground);
        currentBackground = newTexture;
    }

    @Override
    public Actor getActor() {
        return label;
    }

    @Override
    public void dispose() {

        cache.releaseFont(font);
    }

    /**
     * Get the wrapped gdx label.
     *
     * <p>
     *     Required for modal dialog.
     * </p>
     */
    Label getLabel() {

        return label;
    }
}
