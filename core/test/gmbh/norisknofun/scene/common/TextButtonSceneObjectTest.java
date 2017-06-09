package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;


import gmbh.norisknofun.GdxTest;
import gmbh.norisknofun.assets.AssetFactory;
import gmbh.norisknofun.assets.AssetTextButton;

import static org.junit.Assert.*;
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
