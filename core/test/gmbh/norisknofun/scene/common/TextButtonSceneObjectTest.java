package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

import org.junit.Test;
import org.mockito.Mockito;


import gmbh.norisknofun.GdxTest;

import static org.junit.Assert.*;


public class TextButtonSceneObjectTest extends GdxTest{



    @Test
    public void getButtonReturnsCorrectTextButton() {
        TextButton textButton = Mockito.mock(TextButton.class);

        TextButtonSceneObject sceneObject = new TextButtonSceneObject(textButton);

        assertSame(textButton, sceneObject.getButton());
    }

    @Test
    public void setBoundsSetsPositionCorrectly() {
        TextButton textButton = Mockito.mock(TextButton.class);

        TextButtonSceneObject sceneObject = new TextButtonSceneObject(textButton);

        sceneObject.setBounds(2,3,1,1);
        assertEquals(2, (int)sceneObject.getX());
        assertEquals(3, (int)sceneObject.getY());
    }

    @Test
    public void setBoundsSetsSizeCorrectly() {
        TextButton textButton = Mockito.mock(TextButton.class);

        TextButtonSceneObject sceneObject = new TextButtonSceneObject(textButton);

        sceneObject.setBounds(1,1,2,3);
        assertEquals(2, (int)sceneObject.getWidth());
        assertEquals(3, (int)sceneObject.getHeight());
    }

}
