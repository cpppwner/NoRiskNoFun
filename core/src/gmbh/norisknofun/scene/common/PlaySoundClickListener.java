package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import gmbh.norisknofun.assets.AssetSound;

/**
 * Click listener playing sound given in constructor when click event is fired.
 */
public class PlaySoundClickListener extends ClickListener {

    /**
     * The sound asset to play when clicked.
     */
    private final AssetSound sound;

    /**
     * Initialize click listener with given sound.
     * @param sound The sound to play when click event is fired.
     *              The caller is responsible to ensure the sound is available as long as this listener
     *              is and to destroy the asset when no longer needed.
     */
    public PlaySoundClickListener(AssetSound sound) {
        this.sound = sound;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        sound.play();
    }
}
