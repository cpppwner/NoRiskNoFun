package gmbh.norisknofun.assets.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import gmbh.norisknofun.assets.AssetSound;

/**
 * Default implementation of {@link AssetSound}.
 *
 * <p>
 *     This relies on libgdx.
 * </p>
 */
class AssetSoundImpl implements AssetSound {

    /**
     * Sound's filename.
     */
    private final String filename;

    /**
     * Wrapped libGdx sound.
     */
    private final Sound sound;

    /**
     * Create texture asset from given filename.
     */
    AssetSoundImpl(String filename) {

        this.filename = filename;
        this.sound = Gdx.audio.newSound(Gdx.files.internal(filename));
    }

    @Override
    public String getName() {
        return filename;
    }


    @Override
    public void play() {
        sound.play();
    }

    @Override
    public void stop() {
        sound.stop();
    }

    @Override
    public void dispose() {
        sound.dispose();
    }
}
