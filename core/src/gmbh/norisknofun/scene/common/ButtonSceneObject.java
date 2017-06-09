package gmbh.norisknofun.scene.common;

import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.scene.SceneObject;

/**
 * Base object for button scene object.
 */
abstract class ButtonSceneObject extends SceneObject {

    /**
     * Default constructor.
     */
    ButtonSceneObject() {
        this(null);
    }

    /**
     * Constructor taking sound asset, which is played upon button press.
     *
     * @param sound The sound to be played, when the button is pressed.
     *              Note: The client holding such an instance is responsible for disposing the sound.
     */
    ButtonSceneObject(AssetSound sound) {
        if (sound != null) {
            this.addListener(new PlaySoundClickListener(sound));
        }
    }
}
