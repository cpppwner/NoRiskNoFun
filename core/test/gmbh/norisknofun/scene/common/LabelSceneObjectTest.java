package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.scenes.scene2d.Actor;

import org.junit.Before;
import org.junit.Test;

import gmbh.norisknofun.assets.AssetLabel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;


public class LabelSceneObjectTest {

    private AssetLabel mockAssetLabel;

    @Before
    public void setUp() {

        Actor mockActor = mock(Actor.class);
        mockAssetLabel = mock(AssetLabel.class);
        doReturn(mockActor).when(mockAssetLabel).getActor();
    }

    @Test
    public void setBoundsSetsPositionCorrectly() {

        LabelSceneObject sceneObject = new LabelSceneObject(mockAssetLabel);

        sceneObject.setBounds(2,3,1,1);
        assertEquals(2, (int)sceneObject.getX());
        assertEquals(3, (int)sceneObject.getY());
    }

    @Test
    public void setBoundsSetsSizeCorrectly() {

        LabelSceneObject sceneObject = new LabelSceneObject(mockAssetLabel);

        sceneObject.setBounds(1,1,2,3);
        assertEquals(2, (int)sceneObject.getWidth());
        assertEquals(3, (int)sceneObject.getHeight());
    }
}
