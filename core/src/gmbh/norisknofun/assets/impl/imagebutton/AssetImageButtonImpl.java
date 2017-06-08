package gmbh.norisknofun.assets.impl.imagebutton;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import gmbh.norisknofun.assets.AssetImageButton;

/**
 * Default implementation of {@link AssetImageButton}.
 */
public class AssetImageButtonImpl implements AssetImageButton {

    /**
     * Texture filename used for the image button.
     */
    private final String textureFilename;
    /**
     * Texture used for drawing.
     */
    private final Texture texture;
    /**
     * Encapsulated image button.
     */
    private final ImageButton imageButton;

    /**
     * Initialize image button asset.
     *
     * @param  textureFilename Texture's filename.
     */
    public AssetImageButtonImpl(String textureFilename) {

        this.textureFilename = textureFilename;
        texture = new Texture(Gdx.files.internal(textureFilename));
        imageButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(texture)));
    }

    @Override
    public String getName() {
        return textureFilename;
    }

    @Override
    public float getX() {
        return imageButton.getX();
    }

    @Override
    public float getY() {
        return imageButton.getY();
    }

    @Override
    public float getWidth() {
        return imageButton.getWidth();
    }

    @Override
    public float getHeight() {
        return imageButton.getHeight();
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        imageButton.setBounds(x, y, width, height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        imageButton.draw(batch, parentAlpha);
    }

    @Override
    public void dispose() {

        texture.dispose();
    }
}
