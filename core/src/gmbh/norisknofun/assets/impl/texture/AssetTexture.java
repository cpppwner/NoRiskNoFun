package gmbh.norisknofun.assets.impl.texture;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

import gmbh.norisknofun.assets.Asset;

/**
 * Asset for textures.
 */
public class AssetTexture implements Asset, Disposable {

    private final Texture texture;

    AssetTexture(Texture texture) {

        this.texture = texture;
    }


    @Override
    public String getName() {
        return null;
    }

    public Texture getTexture() {
        return texture;
    }

    @Override
    public void dispose() {

        texture.dispose();
    }
}
