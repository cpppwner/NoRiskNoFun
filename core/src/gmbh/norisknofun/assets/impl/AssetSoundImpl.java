package gmbh.norisknofun.assets.impl;

import com.badlogic.gdx.audio.Sound;

import gmbh.norisknofun.assets.AssetSound;

/**
 * Default implementation of {@link AssetSound}.
 */
class AssetSoundImpl implements AssetSound {

    /**
     * Asset cache.
     */
    private final LibGdxAssetCache cache;

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
    AssetSoundImpl(LibGdxAssetCache cache, String filename) {

        this.cache = cache;
        this.filename = filename;
        this.sound = cache.getSound(filename);
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
        cache.releaseSound(sound);
    }
}
