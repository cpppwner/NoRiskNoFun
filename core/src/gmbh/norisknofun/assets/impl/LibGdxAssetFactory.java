package gmbh.norisknofun.assets.impl;

import com.badlogic.gdx.graphics.Color;

import gmbh.norisknofun.assets.AssetFactory;
import gmbh.norisknofun.assets.AssetLabel;
import gmbh.norisknofun.assets.AssetMap;
import gmbh.norisknofun.assets.AssetModalDialog;
import gmbh.norisknofun.assets.AssetNumericField;
import gmbh.norisknofun.assets.AssetPixmapTexture;
import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.assets.AssetTextButton;
import gmbh.norisknofun.assets.AssetTextField;
import gmbh.norisknofun.assets.AssetTexture;
import gmbh.norisknofun.assets.FontDescriptor;
import gmbh.norisknofun.assets.ModalDialogDescriptor;
import gmbh.norisknofun.assets.TextButtonDescriptor;
import gmbh.norisknofun.assets.TextFieldDescriptor;
import gmbh.norisknofun.assets.impl.map.AssetLoaderMap;

/**
 * Default implementation of {@link gmbh.norisknofun.assets.AssetFactory}.
 */
public class LibGdxAssetFactory implements AssetFactory {

    /** low level cache for caching memory consumptive gdx objects */
    private final LibGdxAssetCache cache = new LibGdxAssetCache();

    @Override
    public AssetMap createAssetMap(String filename) {

        return new AssetLoaderMap().load(filename);
    }

    @Override
    public AssetTexture createAssetTexture(String filename) {

        return new AssetTextureImpl(cache, filename);
    }

    @Override
    public AssetPixmapTexture createAssetPixmapTexture(Color color) {

        return new AssetPixmapTextureImpl(cache, color);
    }

    @Override
    public AssetSound createAssetSound(String filename) {

        return new AssetSoundImpl(cache, filename);
    }

    @Override
    public AssetLabel createAssetLabel(String text, FontDescriptor fontDescriptor) {

        return new AssetLabelImpl(cache, text, fontDescriptor);
    }

    @Override
    public AssetImageButtonImpl createAssetImageButton(String textureFilename) {

        return new AssetImageButtonImpl(cache, textureFilename);
    }

    @Override
    public AssetTextButton createAssetTextButton(String initialButtonText, TextButtonDescriptor textButtonDescriptor) {

        return new AssetTextButtonImpl(cache, initialButtonText, textButtonDescriptor);
    }

    @Override
    public AssetTextField createAssetTextField(String initialFieldText, TextFieldDescriptor textFieldDescriptor) {

        return new AssetTextFieldImpl(cache, initialFieldText, textFieldDescriptor);
    }

    @Override
    public AssetNumericField createAssetNumericField(int initialFieldValue, TextFieldDescriptor textFieldDescriptor) {

        return new AssetNumericFieldImpl(cache, initialFieldValue, textFieldDescriptor);
    }

    @Override
    public AssetModalDialog createAssetModalDialog(String dialogText, ModalDialogDescriptor modalDialogDescriptor) {

        return new AssetModalDialogImpl(cache, dialogText, modalDialogDescriptor);
    }
}
