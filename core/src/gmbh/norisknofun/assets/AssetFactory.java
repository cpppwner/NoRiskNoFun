package gmbh.norisknofun.assets;

import com.badlogic.gdx.graphics.Color;

/**
 * Abstract factory to create appropriate assets.
 *
 * <p>
 *     Makes testing of GUI scenes easier, since we are encapsulating libgdx related things in assets.
 *     Assets might be real assets like textures and sounds or GUI widgets.
 * </p>
 */
public interface AssetFactory {

    /**
     * Create map asset.
     *
     * @param filename The map filename which is local to assets directory.
     * @return Newly created map asset.
     */
    AssetMap createAssetMap(String filename);

    /**
     * Create texture asset.
     *
     * @param filename The map filename which is local to assets directory.
     * @return Newly created texture asset.
     */
    AssetTexture createAssetTexture(String filename);

    /**
     * Create pixmap texture asset.
     *
     * <p>
     *     The wrapped pixmap is 1x1 pixels in size.
     * </p>
     *
     * @param color The color used for the pixmap texture.
     * @return Newly created pixmap texture asset.
     */
    AssetPixmapTexture createAssetPixmapTexture(Color color);

    /**
     * Create sound asset.
     *
     * @param filename The map filename which is local to assets directory.
     * @return Newly created sound asset.
     */
    AssetSound createAssetSound(String filename);

    /**
     * Create a label asset using the default font (see assets/font).
     *
     * @param initialLabelText The initial label text.
     * @param fontDescriptor Descriptor object used to specify the font of the label.
     *
     * @return Newly created Label asset.
     */
    AssetLabel createAssetLabel(String initialLabelText, FontDescriptor fontDescriptor);

    /**
     * Create image button asset from given texture filename.
     *
     * @param textureFilename The texture filename used for rendering the image button.
     * @return Newly created image button asset.
     */
    AssetImageButton createAssetImageButton(String textureFilename);

    /**
     * Create text button asset for given initial button text and descriptor.
     *
     * @param initialButtonText The initial button text.
     * @param textButtonDescriptor Container object describing text button styles.
     * @return Newly created text button asset.
     */
    AssetTextButton createAssetTextButton(String initialButtonText, TextButtonDescriptor textButtonDescriptor);

    /**
     * Create text field asset for given initial field text and descriptor.
     *
     * @param initialFieldText Initial text in the text field.
     * @param textFieldDescriptor Container object describing text field styles.
     * @return Newly created text field asset.
     */
    AssetTextField createAssetTextField(String initialFieldText, TextFieldDescriptor textFieldDescriptor);

    /**
     * Create a numeric text field asset showing initial field value and descriptor.
     *
     * @param initialFieldValue The initial value displayed in the text field.
     * @param textFieldDescriptor Container object describing text field styles.
     * @return Newly created numeric field asset.
     */
    AssetNumericField createAssetNumericField(int initialFieldValue, TextFieldDescriptor textFieldDescriptor);

    /**
     * Create modal dialog asset.
     *
     * <p>
     *     This basically creates a modal dialog with header text (see the descriptor),
     *     the text to be shown and an OK button.
     *     So this should be sufficient for error/info dialogs.
     * </p>
     *
     * @param dialogText The text to show in the modal dialog.
     * @param modalDialogDescriptor Container object describing some modal dialog layout.
     * @return Newly created modal dialog asset.
     */
    AssetModalDialog createAssetModalDialog(String dialogText, ModalDialogDescriptor modalDialogDescriptor);
}
