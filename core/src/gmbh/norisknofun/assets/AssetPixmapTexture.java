package gmbh.norisknofun.assets;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

/**
 * Special 1 pixel wide and 1 pixel height texture.
 */
public interface AssetPixmapTexture  extends Asset, Disposable {

    Texture getTexture();
}
