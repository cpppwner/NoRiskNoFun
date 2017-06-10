package gmbh.norisknofun.assets.impl;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import gmbh.norisknofun.assets.AssetModalDialog;
import gmbh.norisknofun.assets.ModalDialogDescriptor;

/**
 * Default implementation of {@link AssetModalDialog}.
 */
class AssetModalDialogImpl implements AssetModalDialog {

    private static final String BUTTON_TEXT = "OK";

    private final LibGdxAssetCache cache;
    private final ModalDialogDescriptor modalDialogDescriptor;
    private final BitmapFont titleFont;
    private final Texture backgroundTexture;
    private final AssetLabelImpl textLabel;
    private final AssetTextButtonImpl textButton;
    private final Dialog dialog;

    AssetModalDialogImpl(LibGdxAssetCache cache, String dialogText, ModalDialogDescriptor modalDialogDescriptor) {

        this.cache = cache;
        this.modalDialogDescriptor = modalDialogDescriptor;
        titleFont = cache.getFont(modalDialogDescriptor.getTitleFont());
        backgroundTexture = cache.getPixMapTexture(modalDialogDescriptor.getBackgroundColor());
        textLabel = new AssetLabelImpl(cache, dialogText, modalDialogDescriptor.getMessageFont());
        textButton = new AssetTextButtonImpl(cache, BUTTON_TEXT, modalDialogDescriptor.getTextButtonDescriptor());

        dialog = new Dialog(modalDialogDescriptor.getTitle(), createWindowStyle())
                .text(textLabel.getLabel())
                .button(textButton.getTextButton());
    }

    private Window.WindowStyle createWindowStyle() {

        Window.WindowStyle style = new Window.WindowStyle();
        style.background = new TextureRegionDrawable(new TextureRegion(backgroundTexture));
        style.titleFont = titleFont;
        style.titleFontColor = modalDialogDescriptor.getTitleFontColor();

        return style;
    }

    @Override
    public float getX() {

        return dialog.getX();
    }

    @Override
    public float getY() {

        return dialog.getY();
    }

    @Override
    public float getWidth() {

        return dialog.getWidth();
    }

    @Override
    public float getHeight() {

        return dialog.getHeight();
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {

        dialog.setBounds(x, y, width, height);
    }

    @Override
    public Actor getActor() {

        return dialog;
    }

    @Override
    public void dispose() {

        textButton.dispose();
        textLabel.dispose();
        cache.releaseFont(titleFont);
        cache.releasePixMapTexture(backgroundTexture);
    }

    @Override
    public void show(Stage stage) {

        dialog.show(stage);
    }
}
