package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;


public class LabelSceneObjectTest {
    @Test
    public void getButtonReturnsCorrectTextButton() {
        Label label = Mockito.mock(Label.class);

        LabelSceneObject sceneObject = new LabelSceneObject(label);

        assertSame(label, sceneObject.getLabel());
    }

    @Test
    public void setBoundsSetsPositionCorrectly() {
        Label label = Mockito.mock(Label.class);

        LabelSceneObject sceneObject = new LabelSceneObject(label);

        sceneObject.setBounds(2,3,1,1);
        assertEquals(2, (int)sceneObject.getX());
        assertEquals(3, (int)sceneObject.getY());
    }

    @Test
    public void setBoundsSetsSizeCorrectly() {
        Label label = Mockito.mock(Label.class);

        LabelSceneObject sceneObject = new LabelSceneObject(label);

        sceneObject.setBounds(1,1,2,3);
        assertEquals(2, (int)sceneObject.getWidth());
        assertEquals(3, (int)sceneObject.getHeight());
    }
}
