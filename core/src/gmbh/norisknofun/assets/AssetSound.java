package gmbh.norisknofun.assets;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

/**
 * Sound asset.
 *
 * <p>
 *     Our own interface for easier testability.
 * </p>
 */
public interface AssetSound extends Asset, Disposable {

    /**
     * Play the sound.
     */
    void play();

    /**
     * Stop playing sound.
     */
    void stop();
}
