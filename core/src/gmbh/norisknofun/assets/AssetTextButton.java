package gmbh.norisknofun.assets;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by cpppwner on 08.06.17.
 */
public interface AssetTextButton extends Asset, Disposable {
    float getX();

    float getY();

    float getWidth();

    float getHeight();

    void setBounds(float x, float y, float width, float height);

    void draw(Batch batch, float parentAlpha);
}
