package gmbh.norisknofun.assets.impl.label;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import gmbh.norisknofun.assets.AssetFont;
import gmbh.norisknofun.assets.AssetLabel;

/**
 * Default implementation of {@link AssetLabel}.
 */
public class AssetLabelImpl implements AssetLabel {

    private final Label label;
    private final AssetFont font;

    public AssetLabelImpl(String text, AssetFont font) {

        label = new Label(text, new Label.LabelStyle(font.toBitmapFont(), font.toBitmapFont().getColor()));
        this.font = font;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        label.draw(batch, parentAlpha);
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
