package gmbh.norisknofun.assets.impl;

import com.badlogic.gdx.graphics.Color;

import gmbh.norisknofun.assets.AssetFactory;
import gmbh.norisknofun.assets.AssetLabel;
import gmbh.norisknofun.assets.AssetMap;
import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.assets.AssetTexture;
import gmbh.norisknofun.assets.impl.label.AssetLabelImpl;
import gmbh.norisknofun.assets.impl.map.AssetLoaderMap;
import gmbh.norisknofun.assets.impl.sound.AssetSoundImpl;
import gmbh.norisknofun.assets.impl.texture.AssetTextureImpl;

/**
 * Default implementation of {@link gmbh.norisknofun.assets.AssetFactory}.
 */
public class AssetFactoryImpl implements AssetFactory {

    @Override
    public AssetMap createAssetMap(String filename) {

        return new AssetLoaderMap().load(filename);
    }

    @Override
    public AssetTexture createAssetTexture(String filename) {

        return new AssetTextureImpl(filename);
    }

    @Override
    public AssetSound createAssetSound(String filename) {

        return new AssetSoundImpl(filename);
    }

    @Override
    public AssetLabel createAssetLabel(String text, int fontSize, Color color, float borderWidth) {

        return new AssetLabelImpl(text, fontSize, color, borderWidth);
    }
}
