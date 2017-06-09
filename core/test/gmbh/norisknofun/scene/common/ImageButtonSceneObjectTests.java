package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.scenes.scene2d.Actor;

import org.junit.Before;
import org.junit.Test;

import gmbh.norisknofun.assets.AssetImageButton;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;


public class ImageButtonSceneObjectTests {

    private AssetImageButton mockAssetImageButton;

    @Before
    public void setUp() {

        Actor mockActor = mock(Actor.class);
        mockAssetImageButton = mock(AssetImageButton.class);
        doReturn(mockActor).when(mockAssetImageButton).getActor();
    }

    @Test
    public void setBoundsSetsPositionCorrectly() {

        ImageButtonSceneObject sceneObject = new ImageButtonSceneObject(mockAssetImageButton);

        sceneObject.setBounds(2,3,1,1);
        assertEquals(2, (int)sceneObject.getX());
        assertEquals(3, (int)sceneObject.getY());
    }

    @Test
    public void setBoundsSetsSizeCorrectly() {

        ImageButtonSceneObject sceneObject = new ImageButtonSceneObject(mockAssetImageButton);

        sceneObject.setBounds(1,1,2,3);
        assertEquals(2, (int)sceneObject.getWidth());
        assertEquals(3, (int)sceneObject.getHeight());
    }
}
