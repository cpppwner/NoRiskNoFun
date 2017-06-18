package gmbh.norisknofun.scene.common;

import org.junit.Ignore;
import org.junit.Test;

import gmbh.norisknofun.GdxTest;
import gmbh.norisknofun.assets.AssetTextButton;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;


@Ignore("TODO refactor")
public class TextButtonSceneObjectTest extends GdxTest{


    @Test
    public void setBoundsSetsPositionCorrectly() {

        AssetTextButton wrappedAsset = mock(AssetTextButton.class);

        TextButtonSceneObject sceneObject = new TextButtonSceneObject(wrappedAsset);

        sceneObject.setBounds(2,3,1,1);
        assertEquals(2, (int)sceneObject.getX());
        assertEquals(3, (int)sceneObject.getY());
    }

    @Test
    public void setBoundsSetsSizeCorrectly() {

        AssetTextButton wrappedAsset = mock(AssetTextButton.class);

        TextButtonSceneObject sceneObject = new TextButtonSceneObject(wrappedAsset);

        sceneObject.setBounds(1,1,2,3);
        assertEquals(2, (int)sceneObject.getWidth());
        assertEquals(3, (int)sceneObject.getHeight());
    }

}
