package gmbh.norisknofun.assets.impl;

import com.badlogic.gdx.scenes.scene2d.ui.TextField;

import gmbh.norisknofun.assets.AssetNumericField;
import gmbh.norisknofun.assets.TextFieldDescriptor;

/**
 * Default numeric field.
 *
 * <p>
 *     This is based on the libgdx text field with an appropriate filter set.
 * </p>
 */
class AssetNumericFieldImpl extends AssetTextFieldImpl implements AssetNumericField {

    AssetNumericFieldImpl(LibGdxAssetCache cache, int initialFieldValue, TextFieldDescriptor textFieldDescriptor) {

        super(cache, Integer.toString(initialFieldValue), textFieldDescriptor);

        setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
    }

    @Override
    public int getValue() {

        return getText().isEmpty()
                ? 0
                : Integer.parseInt(getText());
    }

}
