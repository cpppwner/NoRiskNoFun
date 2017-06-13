package gmbh.norisknofun.assets;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;

/**
 * Modal dialog asset for displaying error/info messages.
 */
public interface AssetModalDialog extends AssetWidget, Disposable {

    /**
     * Show the error dialog on top of the given stage.
     *
     * @param stage Stage used for rendering this modal error dialog.
     */
    void show(Stage stage);
}
