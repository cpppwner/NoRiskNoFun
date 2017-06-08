package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;

import gmbh.norisknofun.assets.AssetFactory;
import gmbh.norisknofun.assets.AssetTexture;
import gmbh.norisknofun.scene.SceneObject;

/**
 * Special background scene object.
 *
 * <p>
 *     This is used to render a common background object in GUI scenes.
 * </p>
 */
public final class BackgroundSceneObject extends SceneObject {

    private static final String DEFAULT_BACKGROUND_IMAGE = "img/menu.png";

    /**
     * Texture drawn in background.
     */
    private final AssetTexture texture;

    /**
     * Initialize this scene object.
     * @param assetFactory AssetFactory used to load background texture.
     */
    public BackgroundSceneObject(AssetFactory assetFactory) {

        super();
        texture = assetFactory.createAssetTexture(DEFAULT_BACKGROUND_IMAGE);

        super.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha){

        texture.draw(batch, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void dispose() {

        texture.dispose();
        super.dispose();
    }
}
