package gmbh.norisknofun.assets;

/**
 * Abstract factory to create appropriate assets.
 *
 * <p>
 *     Makes testing of GUI scenes easier, since we are encapsulating libgdx related things in assets.
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
     *
     * @param textButtonDescriptor
     * @return
     */
    AssetTextButton createAssetTextButton(String initialButtonText, TextButtonDescriptor textButtonDescriptor);

    AssetTextField createAssetTextField(String initialFieldText, TextFieldDescriptor textFieldDescriptor);
}
