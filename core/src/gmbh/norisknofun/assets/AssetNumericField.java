package gmbh.norisknofun.assets;

import com.badlogic.gdx.utils.Disposable;

/**
 * Special text field asset for numeric input onlu.
 */
public interface AssetNumericField extends AssetWidget, Disposable {

    /**
     * Get text field's name.
     */
    String getName();

    /**
     * Get the numeric value.
     */
    int getValue();
}
