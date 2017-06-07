package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import org.junit.Test;
import org.mockito.Mockito;

import gmbh.norisknofun.assets.AssetLabel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;


public class LabelSceneObjectTest {

    @Test
    public void setBoundsSetsPositionCorrectly() {
        AssetLabel label = Mockito.mock(AssetLabel.class);

        LabelSceneObject sceneObject = new LabelSceneObject(label);

        sceneObject.setBounds(2,3,1,1);
        assertEquals(2, (int)sceneObject.getX());
        assertEquals(3, (int)sceneObject.getY());
    }

    @Test
    public void setBoundsSetsSizeCorrectly() {
        AssetLabel label = Mockito.mock(AssetLabel.class);

        LabelSceneObject sceneObject = new LabelSceneObject(label);

        sceneObject.setBounds(1,1,2,3);
        assertEquals(2, (int)sceneObject.getWidth());
        assertEquals(3, (int)sceneObject.getHeight());
    }
}
