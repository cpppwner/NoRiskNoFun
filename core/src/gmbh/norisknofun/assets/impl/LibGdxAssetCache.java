package gmbh.norisknofun.assets.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

import gmbh.norisknofun.assets.FontDescriptor;

/**
 * Simple asset cache for caching things like textures, sounds and such.
 *
 * <p>
 *     To not waste up too much memory for those things we' ll cache them.
 * </p>
 */
class LibGdxAssetCache {

    /** map for caching textures loaded from file (filename is the key) */
    private final Map<String, RefCountedAsset<Texture>> textureCache = new HashMap<>();
    /** map for caching sounds loaded from file (filename is the key) */
    private final Map<String, RefCountedAsset<Sound>> soundCache = new HashMap<>();
    /** map for caching generated bitmap fonts */
    private final Map<FontDescriptor, RefCountedAsset<BitmapFont>> fontCache = new HashMap<>();
    /** map for caching generated pixmap textures */
    private final Map<Color, RefCountedAsset<Texture>> pixMapTextureCache = new HashMap<>();

    /**
     * Get texture from cache.
     *
     * <p>
     *     If no texture with given filename was loaded up to now, a new one is created
     *     and added to the cache.
     *     For each call to this method the method {@link LibGdxAssetCache#releaseTexture(Texture)}
     *     must be called.
     * </p>
     *
     * @param textureFilename Texture's filename.
     * @return Cached texture.
     */
    Texture getTexture(String textureFilename) {

        RefCountedAsset<Texture> value = textureCache.get(textureFilename);
        if (value == null) {
            value = new RefCountedAsset<>(new Texture(Gdx.files.internal(textureFilename)));
            textureCache.put(textureFilename, value);
        }

        return value.get();
    }

    /**
     * Releases a texture.
     * @param texture The texture retrieved via {@link LibGdxAssetCache#getTexture(String)}.
     */
    void releaseTexture(Texture texture) {

        release(textureCache, texture);
    }

    /**
     * Get sound from cache.
     *
     * <p>
     *     If no sound with given filename was loaded up to now, a new one is created
     *     and added to the cache.
     *     For each call to this method the method {@link LibGdxAssetCache#releaseSound(Sound)}
     *     must be called.
     * </p>
     *
     * @param soundFilename Sound's filename.
     * @return Cached sound.
     */
    Sound getSound(String soundFilename) {

        RefCountedAsset<Sound> value = soundCache.get(soundFilename);
        if (value == null) {
            value = new RefCountedAsset<>(Gdx.audio.newSound(Gdx.files.internal(soundFilename)));
            soundCache.put(soundFilename, value);
        }

        return value.get();
    }

    /**
     * Releases a sound.
     * @param sound The sound retrieved via {@link LibGdxAssetCache#getSound(String)}.
     */
    void releaseSound(Sound sound) {

        release(soundCache, sound);
    }

    /**
     * Get bitmap font from cache.
     *
     * <p>
     *     If bitmap font is not cached for given descriptor, a new one is created and added to
     *     the internal cache.
     * </p>
     *
     * @param fontDescriptor Object describing the font.
     * @return Cached font.
     */
    BitmapFont getFont(FontDescriptor fontDescriptor) {

        RefCountedAsset<BitmapFont> value = fontCache.get(fontDescriptor);
        if (value == null) {
            value = new RefCountedAsset<>(generateFont(fontDescriptor));
            fontCache.put(fontDescriptor, value);
        }

        return value.get();
    }

    /**
     * Generate new bitmap font.
     *
     * @param fontDescriptor Descriptor object describing font properties.
     * @return Newly generated bitmap font.
     */
    private static BitmapFont generateFont(FontDescriptor fontDescriptor) {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontDescriptor.getFontFilename()));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.color = fontDescriptor.getForegroundColor();
        parameter.size = fontDescriptor.getFontSize();
        parameter.borderWidth = fontDescriptor.getBorderWidth();
        parameter.borderColor = fontDescriptor.getBorderColor();
        parameter.borderStraight = fontDescriptor.isStraightBorderUsed();
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose();

        return font;
    }

    /**
     * Releases a bitmap font.
     * @param font The font retrieved via {@link LibGdxAssetCache#getFont(FontDescriptor)}.
     */
    void releaseFont(BitmapFont font) {

        release(fontCache, font);
    }

    /**
     * Get cached pixmap texture for given color
     *
     * <p>
     *     If no pixmap texture for given color is in the cache, a new one is created
     *     and added to the internal cache.
     * </p>
     * <p>
     *     All pixmap textures are 1x1 pixels with RGBA8888 format.
     * </p>
     *
     * @param color The color of the pixmap texture.
     * @return Cached pixmap texture.
     */
    Texture getPixMapTexture(Color color) {

        RefCountedAsset<Texture> value = pixMapTextureCache.get(color);
        if (value == null) {
            value = new RefCountedAsset<>(createPixMapTexture(color));
            pixMapTextureCache.put(color, value);
        }

        return value.get();
    }

    /**
     * Create 1x1 pixel pixmap texture for given color.
     * @param color The color used to fill the texture with.
     * @return Newly created pixmap texture.
     */
    private static Texture createPixMapTexture(Color color) {

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        return new Texture(new PixmapTextureData(pixmap, null, false, true));
    }

    /**
     * Releases a pixmap texture.
     * @param texture The pixmap texture retrieved via {@link LibGdxAssetCache#getPixMapTexture(Color)}.
     */
    void releasePixMapTexture(Texture texture) {

        release(pixMapTextureCache, texture);
    }

    /**
     * Helper method to release a cached asset (texture, sound, font, ...).
     *
     * <p>
     *     If {@link RefCountedAsset#release()} returns {@code true}, the asset is also
     *     removed from the cache.
     * </p>
     *
     * @param cache The cache where to search for the asset to be released.
     * @param valueToRelease The asset to release.
     * @param <KeyType> Key type in the {@code Map}.
     * @param <AssetType> The asset type.
     */
    private static <KeyType, AssetType extends Disposable> void release(Map<KeyType, RefCountedAsset<AssetType>> cache,
                                                                AssetType valueToRelease) {

        KeyType keyToRemove = null;
        for (Map.Entry<KeyType, RefCountedAsset<AssetType>> entry : cache.entrySet()) {
            if (entry.getValue().wrapsAsset(valueToRelease)) {
                if (entry.getValue().release()) {
                    keyToRemove = entry.getKey();
                }
                break;
            }
        }

        if (keyToRemove != null) {
            cache.remove(keyToRemove);
        }
    }

    /**
     * Helper class providing simple reference counting.
     *
     * @param <AssetType> The wrapped asset type (texture, sound, bitmap font).
     */
    private static final class RefCountedAsset<AssetType extends Disposable> {

        /** the wrapped asset */
        private final AssetType asset;
        /** reference count */
        private int refCount;

        /**
         * Constructor receiving the wrapped asset.
         *
         * <p>
         *     The reference count is set to 0.
         * </p>
         */
        private RefCountedAsset(AssetType asset) {

            this.asset = asset;
            refCount = 0;
        }

        /**
         * Tests if the asset wrapped by this class is same as (reference equals)
         * the given parameter.
         *
         * @param asset The asset to check if it's wrapped by this class.
         * @return {@code true} if this class wraps the given asset, {@code false} otherwise.
         */
        private boolean wrapsAsset(AssetType asset) {

            return this.asset == asset; // reference equality
        }

        /**
         * Get the wrapped asset and increment the reference count by one.
         */
        private AssetType get() {
            refCount += 1;
            return asset;
        }

        /**
         * Release the wrapped asset.
         *
         * <p>
         *     Releasing means, decrement the reference count and dispose the resource
         *     if the ref count is equal to 0.
         * </p>
         *
         * @return {@code true} if wrapped asset was disposed, {@code false} otherwise.
         */
        private boolean release() {
            refCount -= 1;
            if (refCount == 0) {
                asset.dispose();
            }

            return refCount == 0;
        }
    }
}
