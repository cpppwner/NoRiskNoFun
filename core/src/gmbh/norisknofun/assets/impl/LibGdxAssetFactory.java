package gmbh.norisknofun.assets.impl;

import gmbh.norisknofun.assets.AssetFactory;
import gmbh.norisknofun.assets.AssetLabel;
import gmbh.norisknofun.assets.AssetMap;
import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.assets.AssetTextButton;
import gmbh.norisknofun.assets.AssetTextField;
import gmbh.norisknofun.assets.AssetTexture;
import gmbh.norisknofun.assets.FontDescriptor;
import gmbh.norisknofun.assets.TextButtonDescriptor;
import gmbh.norisknofun.assets.TextFieldDescriptor;
import gmbh.norisknofun.assets.impl.imagebutton.AssetImageButtonImpl;
import gmbh.norisknofun.assets.impl.label.AssetLabelImpl;
import gmbh.norisknofun.assets.impl.map.AssetLoaderMap;
import gmbh.norisknofun.assets.impl.sound.AssetSoundImpl;
import gmbh.norisknofun.assets.impl.textbutton.AssetTextButtonImpl;
import gmbh.norisknofun.assets.impl.textfield.AssetTextFieldImpl;
import gmbh.norisknofun.assets.impl.texture.AssetTextureImpl;

/**
 * Default implementation of {@link gmbh.norisknofun.assets.AssetFactory}.
 */
public class LibGdxAssetFactory implements AssetFactory {

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
    public AssetLabel createAssetLabel(String text, FontDescriptor fontDescriptor) {

        return new AssetLabelImpl(text, fontDescriptor);
    }

    @Override
    public AssetImageButtonImpl createAssetImageButton(String textureFilename) {

        return new AssetImageButtonImpl(textureFilename);
    }

    @Override
    public AssetTextButton createAssetTextButton(String initialButtonText, TextButtonDescriptor textButtonDescriptor) {

        return new AssetTextButtonImpl(initialButtonText, textButtonDescriptor);
    }

    @Override
    public AssetTextField createAssetTextField(TextFieldDescriptor textFieldDescriptor) {

        return new AssetTextFieldImpl(textFieldDescriptor);
    }
}
