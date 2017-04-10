package gmbh.norisknofun.assets.impl.map;

import com.google.gson.Gson;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import gmbh.norisknofun.GdxTestRunner;
import gmbh.norisknofun.assets.Asset;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class AssetLoaderMapTests extends GdxTestRunner {

    @Test(expected = IllegalArgumentException.class)
    public void loadingMapWithNullInputStreamThrowsAnException() {

        // given
        AssetLoaderMap target = new AssetLoaderMap();

        // when/then
        target.load(null);
    }

    @Test
    public void loadingMapWithInvalidJsonGivesNull() {

        // given
        AssetLoaderMap target = new AssetLoaderMap();

        // when
        Asset obtained = target.load(new ByteArrayInputStream("asdf".getBytes()));

        // then
        assertThat(obtained, is(nullValue()));
    }

    @Test
    public void loadingMapWithNameSetToNullGivesNull() {

        // given
        AssetLoaderMap target = new AssetLoaderMap();
        GameMap map = createGameMap();

        // when
        map.name = null;
        Asset obtained = target.load(new ByteArrayInputStream(new Gson().toJson(map).getBytes()));

        // then
        assertThat(obtained, is(nullValue()));
    }

    @Test
    public void loadingMapWithNameSetToEmptyStringGivesNull() {

        // given
        AssetLoaderMap target = new AssetLoaderMap();
        GameMap map = createGameMap();

        // when
        map.name = "";
        Asset obtained = target.load(new ByteArrayInputStream(new Gson().toJson(map).getBytes()));

        // then
        assertThat(obtained, is(nullValue()));
    }

    @Test
    public void loadingMapWithVerticesSetToNullGivesNull() {

        // given
        AssetLoaderMap target = new AssetLoaderMap();
        GameMap map = createGameMap();

        // when
        map.vertices = null;
        Asset obtained = target.load(new ByteArrayInputStream(new Gson().toJson(map).getBytes()));

        // then
        assertThat(obtained, is(nullValue()));
    }

    @Test
    public void loadingMapWithoutVerticesGivesNull() {

        // given
        AssetLoaderMap target = new AssetLoaderMap();
        GameMap map = createGameMap();

        // when
        map.vertices.clear();
        Asset obtained = target.load(new ByteArrayInputStream(new Gson().toJson(map).getBytes()));

        // then
        assertThat(obtained, is(nullValue()));
    }

    @Test
    public void loadingMapWithRegionsSetToNullGivesNull() {

        // given
        AssetLoaderMap target = new AssetLoaderMap();
        GameMap map = createGameMap();

        // when
        map.regions = null;
        Asset obtained = target.load(new ByteArrayInputStream(new Gson().toJson(map).getBytes()));

        // then
        assertThat(obtained, is(nullValue()));
    }

    @Test
    public void loadingMapWithoutRegionsGivesNull() {

        // given
        AssetLoaderMap target = new AssetLoaderMap();
        GameMap map = createGameMap();

        // when
        map.regions.clear();
        Asset obtained = target.load(new ByteArrayInputStream(new Gson().toJson(map).getBytes()));

        // then
        assertThat(obtained, is(nullValue()));
    }

    @Test
    public void loadingMapWithDuplicateRegionNamesGivesNull() {

        // given
        AssetLoaderMap target = new AssetLoaderMap();
        GameMap map = createGameMap();

        // when
        map.regions.get(1).name = map.regions.get(0).name;
        Asset obtained = target.load(new ByteArrayInputStream(new Gson().toJson(map).getBytes()));

        // then
        assertThat(obtained, is(nullValue()));
    }

    @Test
    public void loadingMapWithUnknownNeighbouringRegionGivesNull() {

        // given
        AssetLoaderMap target = new AssetLoaderMap();
        GameMap map = createGameMap();

        // when
        map.regions.get(0).neighbouringRegions.clear();
        map.regions.get(0).neighbouringRegions.add("42");
        Asset obtained = target.load(new ByteArrayInputStream(new Gson().toJson(map).getBytes()));

        // then
        assertThat(obtained, is(nullValue()));
    }

    @Test
    public void loadingMapWithRegionHavingLessThanFourVerticesGivesNull() {

        // given
        AssetLoaderMap target = new AssetLoaderMap();
        GameMap map = createGameMap();

        // when
        map.regions.get(0).vertexIndices = new ArrayList<>(Arrays.asList(0, 1, 0));
        Asset obtained = target.load(new ByteArrayInputStream(new Gson().toJson(map).getBytes()));

        // then
        assertThat(obtained, is(nullValue()));
    }

    @Test
    public void loadingMapWithValidDataGivesMap() {

        // given
        AssetLoaderMap target = new AssetLoaderMap();
        GameMap map = createGameMap();

        // when
        Asset obtained = target.load(new ByteArrayInputStream(new Gson().toJson(map).getBytes()));

        // then
        assertThat(obtained, is(not(nullValue())));
        assertThat(obtained, is(instanceOf(AssetMap.class)));

        AssetMap obtainedMap = (AssetMap)obtained;
        assertThat(obtainedMap.getName(), is(map.name));
        assertThat(obtainedMap.getRegions().size(), is(map.regions.size()));
        assertThat(obtainedMap.getRegions().stream().map(AssetMap.Region::getName).toArray(),
                    equalTo(map.regions.stream().map(r -> r.name).toArray()));

    }

    private static GameMap createGameMap() {

        GameMap map = new GameMap();
        map.name = "Test map";

        GameMap.Vertex vertexOne = new GameMap.Vertex();
        vertexOne.x = 0.1f;
        vertexOne.x = 0.1f;
        GameMap.Vertex vertexTwo = new GameMap.Vertex();
        vertexTwo.x = 0.5f;
        vertexTwo.y = 0.1f;
        GameMap.Vertex vertexThree = new GameMap.Vertex();
        vertexThree.x = 0.5f;
        vertexThree.y = 0.7f;
        GameMap.Vertex vertexFour = new GameMap.Vertex();
        vertexFour.x = 0.7f;
        vertexFour.y = 0.4f;

        map.vertices = new ArrayList<>(Arrays.asList(vertexOne, vertexTwo, vertexThree, vertexFour));

        GameMap.Region regionOne = new GameMap.Region();
        regionOne.name = "Region One";
        regionOne.vertexIndices = new ArrayList<>(Arrays.asList(0, 1, 2, 0));
        regionOne.neighbouringRegions = new ArrayList<>(Collections.singletonList("Region Two"));

        GameMap.Region regionTwo = new GameMap.Region();
        regionTwo.name = "Region Two";
        regionTwo.vertexIndices = new ArrayList<>(Arrays.asList(1, 2, 3, 1));
        regionTwo.neighbouringRegions = new ArrayList<>(Collections.singletonList("Region One"));

        map.regions = new ArrayList<>(Arrays.asList(regionOne, regionTwo));

        return map;
    }
}
