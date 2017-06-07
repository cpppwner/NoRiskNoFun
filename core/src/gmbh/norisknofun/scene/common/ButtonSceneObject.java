package gmbh.norisknofun.scene.common;

import gmbh.norisknofun.assets.AssetSound;
import gmbh.norisknofun.scene.SceneObject;

/**
 * Base object for button scene object.
 */
abstract class ButtonSceneObject extends SceneObject {

    ButtonSceneObject() {
        this(null);
    }

    ButtonSceneObject(AssetSound sound) {
        if (sound != null) {
            this.addListener(new PlaySoundClickListener(sound));
        }
    }
}
