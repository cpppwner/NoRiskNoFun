package gmbh.norisknofun.assets;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.Disposable;

/**
 * Font asset.
 */
public interface AssetFont extends Asset, Disposable {

    /**
     *
     */
    BitmapFont toBitmapFont();
}
