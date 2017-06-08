package gmbh.norisknofun.assets.impl.label;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import gmbh.norisknofun.assets.AssetLabel;
import gmbh.norisknofun.assets.FontDescriptor;
import gmbh.norisknofun.assets.impl.font.FontGenerator;

/**
 * Default implementation of {@link AssetLabel}.
 */
public class AssetLabelImpl implements AssetLabel {

    private final BitmapFont font;
    private final Label label;

    /**
     * Create a label with given text and font descriptor.
     *
     * @param text The text to show (might change)
     * @param fontDescriptor Container class describing font properties of this label.
     */
    public AssetLabelImpl(String text, FontDescriptor fontDescriptor) {

        font = new FontGenerator(fontDescriptor).generateFont();
        label = new Label(text, new Label.LabelStyle(font, font.getColor()));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        label.draw(batch, parentAlpha);
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
    public void dispose() {

        font.dispose();
    }

    @Override
    public String getName() {

        return label.getText().toString();
    }
}
