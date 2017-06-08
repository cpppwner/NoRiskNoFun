package gmbh.norisknofun.scene.common;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import org.junit.Test;
import org.mockito.Mockito;

import gmbh.norisknofun.assets.AssetImageButton;
import gmbh.norisknofun.assets.AssetTexture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;


public class ImageButtonSceneObjectTests {

    @Test
    public void setBoundsSetsPositionCorrectly() {

        AssetImageButton wrappedAsset = Mockito.mock(AssetImageButton.class);

        ImageButtonSceneObject sceneObject = new ImageButtonSceneObject(wrappedAsset);

        sceneObject.setBounds(2,3,1,1);
        assertEquals(2, (int)sceneObject.getX());
        assertEquals(3, (int)sceneObject.getY());
    }

    @Test
    public void setBoundsSetsSizeCorrectly() {
        AssetImageButton wrappedAsset = Mockito.mock(AssetImageButton.class);

        ImageButtonSceneObject sceneObject = new ImageButtonSceneObject(wrappedAsset);

        sceneObject.setBounds(1,1,2,3);
        assertEquals(2, (int)sceneObject.getWidth());
        assertEquals(3, (int)sceneObject.getHeight());
    }
}
