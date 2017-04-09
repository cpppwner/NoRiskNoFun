package gmbh.norisknofun.assets.impl;

import org.junit.Test;

import gmbh.norisknofun.gmbh.norisknofun.assets.AssetLoader;
import gmbh.norisknofun.gmbh.norisknofun.assets.AssetLoaderFactory;
import gmbh.norisknofun.gmbh.norisknofun.assets.AssetType;
import gmbh.norisknofun.gmbh.norisknofun.assets.gmbh.norisknofun.assets.impl.AssetLoaderFactoryImpl;
import gmbh.norisknofun.gmbh.norisknofun.assets.gmbh.norisknofun.assets.impl.gmbh.norisknofun.assets.impl.map.AssetLoaderMap;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class AssetLoaderFactoryImplTests {

    @Test
    public void createAssetLoaderForAssetTypeMapReturnsCorrectAssetLoader() {

        // given
        AssetLoaderFactory target = new AssetLoaderFactoryImpl();

        // when
        AssetLoader<?> obtained = target.createAssetLoader(AssetType.ASSET_TYPE_MAP);

        // then
        assertThat(obtained, instanceOf(AssetLoaderMap.class));
    }

    @Test
    public void createAssetLoaderMapReturnsCorrectAssetLoader() {

        // given
        AssetLoaderFactory target = new AssetLoaderFactoryImpl();

        // when
        AssetLoader<?> obtained = target.createAssetLoaderMap();

        // then
        assertThat(obtained, instanceOf(AssetLoaderMap.class));
    }
}
