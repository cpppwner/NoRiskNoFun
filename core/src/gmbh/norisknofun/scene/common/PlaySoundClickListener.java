package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import gmbh.norisknofun.assets.AssetSound;

/**
 * Click listener playing sound given in constructor.
 */
public class PlaySoundClickListener extends ClickListener {

    private final AssetSound sound;

    public PlaySoundClickListener(AssetSound sound) {
        this.sound = sound;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        sound.play();
    }
}
