package gmbh.norisknofun.scene;

import org.junit.After;
import org.junit.Test;
import org.mockito.InOrder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import gmbh.norisknofun.GdxTest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class SceneManagerTests extends GdxTest {

    @After
    public void tearDown() {
        SceneManager.getInstance().dispose();
    }

    @Test
    public void getInstanceReturnsSameInstance() {

        // when
        SceneManager sceneManagerOne = SceneManager.getInstance();
        SceneManager sceneManagerTwo = SceneManager.getInstance();

        // then
        assertThat(sceneManagerOne, is(sameInstance(sceneManagerTwo)));
    }

    @Test
    public void getActiveSceneReturnsNotNullValue() {

        // when
        Scene obtained = SceneManager.getInstance().getActiveScene();

        // then
        assertThat(obtained, is(not(nullValue())));
    }

    @Test(expected = IllegalArgumentException.class)
    public void registerNullSceneThrowsException() {

        // when/then
        SceneManager.getInstance().registerScene(null);
    }

    @Test
    public void registerSceneThatHasNotBeenRegisteredYet() {

        // given
        String sceneName = "Mock Scene";
        Scene scene = mock(Scene.class);
        when(scene.getName()).thenReturn(sceneName);

        // when
        boolean obtained = SceneManager.getInstance().registerScene(scene);

        // then
        assertThat(obtained, is(true));
        assertThat(SceneManager.getInstance().getRegisteredScenes(), equalTo(Collections.singleton(sceneName)));
        verify(scene, times(2)).getName();
        verifyNoMoreInteractions(scene);
    }

    @Test
    public void registerSceneWithSameNameAsPreviouslyRegisteredOne() {

        // given
        String sceneName = "Mock Scene";

        Scene sceneOne = mock(Scene.class);
        when(sceneOne.getName()).thenReturn(sceneName);

        Scene sceneTwo = mock(Scene.class);
        when(sceneTwo.getName()).thenReturn(sceneName);

        // when
        boolean obtained = SceneManager.getInstance().registerScene(sceneOne);

        // then
        assertThat(obtained, is(true));
        assertThat(SceneManager.getInstance().getRegisteredScenes(), equalTo(Collections.singleton(sceneName)));
        verify(sceneOne, times(2)).getName();
        verifyNoMoreInteractions(sceneOne);

        // and when
        obtained = SceneManager.getInstance().registerScene(sceneTwo);

        // then
        assertThat(obtained, is(false));
        assertThat(SceneManager.getInstance().getRegisteredScenes(), equalTo(Collections.singleton(sceneName)));
        verify(sceneOne, times(2)).getName();
        verify(sceneTwo, times(1)).getName();
        verifyNoMoreInteractions(sceneOne, sceneTwo);
    }

    @Test
    public void registerMultipleScenes() {

        // given
        String sceneNameOne = "Mock Scene 1";
        Scene sceneOne = mock(Scene.class);
        when(sceneOne.getName()).thenReturn(sceneNameOne);

        String sceneNameTwo = "Mock Scene 2";
        Scene sceneTwo = mock(Scene.class);
        when(sceneTwo.getName()).thenReturn(sceneNameTwo);

        // when
        boolean obtained = SceneManager.getInstance().registerScene(sceneOne);
        obtained &= SceneManager.getInstance().registerScene(sceneTwo);

        // then
        Set<String> expectedSceneNames = new HashSet<>(Arrays.asList(sceneNameOne, sceneNameTwo));
        assertThat(obtained, is(true));
        assertThat(SceneManager.getInstance().getRegisteredScenes(), equalTo(expectedSceneNames));

    }

    @Test(expected = IllegalArgumentException.class)
    public void unregisterSceneWithNullNameThrowsAnException() {

        // when/then
        SceneManager.getInstance().unregisterScene(null);
    }

    @Test
    public void unregisterSceneThatHasNotBeenRegisteredGivesFalse() {

        // when
        boolean obtained = SceneManager.getInstance().unregisterScene("Some Scene");

        // then
        assertThat(obtained, is(false));
    }

    @Test
    public void unregisterSceneRemovesIt() {

        // given
        String sceneName = "Mock Scene";
        Scene scene = mock(Scene.class);
        when(scene.getName()).thenReturn(sceneName);

        SceneManager.getInstance().registerScene(scene);

        // when
        boolean obtained = SceneManager.getInstance().unregisterScene(sceneName);

        // then
        assertThat(obtained, is(true));
        assertThat(SceneManager.getInstance().getRegisteredScenes(), equalTo(Collections.<String>emptySet()));
        verify(scene, times(1)).dispose();
    }

    @Test
    public void unregisterMultipleScenes() {

        // given
        String sceneNameOne = "Mock Scene 1";
        Scene sceneOne = mock(Scene.class);
        when(sceneOne.getName()).thenReturn(sceneNameOne);

        String sceneNameTwo = "Mock Scene 2";
        Scene sceneTwo = mock(Scene.class);
        when(sceneTwo.getName()).thenReturn(sceneNameTwo);

        SceneManager.getInstance().registerScene(sceneOne);
        SceneManager.getInstance().registerScene(sceneTwo);

        // when
        boolean obtained = SceneManager.getInstance().unregisterScene(sceneNameOne);

        // then
        assertThat(obtained, is(true));
        assertThat(SceneManager.getInstance().getRegisteredScenes(), equalTo(Collections.singleton(sceneNameTwo)));
        verify(sceneOne, times(1)).dispose();
        verify(sceneTwo, times(0)).dispose();

        // and when
        obtained = SceneManager.getInstance().unregisterScene(sceneNameTwo);

        // then
        assertThat(obtained, is(true));
        assertThat(SceneManager.getInstance().getRegisteredScenes(), equalTo(Collections.<String>emptySet()));
        verify(sceneOne, times(1)).dispose();
        verify(sceneTwo, times(1)).dispose();
    }

    @Test(expected = IllegalArgumentException.class)
    public void setActiveSceneWithNullArgumentThrowsException() {

        // when/then
        SceneManager.getInstance().setActiveScene(null);
    }

    @Test
    public void setActiveSceneWithNotRegisteredSceneName() {

        // when
        boolean obtained = SceneManager.getInstance().setActiveScene("Dummy Scene");

        // then
        assertThat(obtained, is(false));
    }

    @Test
    public void setActiveSceneWhenSceneIsAlreadyActive() {

        // given
        String sceneName = "Mock Scene 1";
        Scene scene = mock(Scene.class);
        when(scene.getName()).thenReturn(sceneName);

        InOrder inOrder = inOrder(scene);

        SceneManager.getInstance().registerScene(scene);

        // when switching first time to scene
        boolean obtained = SceneManager.getInstance().setActiveScene(sceneName);

        // then
        assertThat(obtained, is(true));
        verify(scene, times(1)).show();
        verify(scene, times(0)).hide();
        inOrder.verify(scene).show();
        inOrder.verify(scene).resize(0, 0);
        assertThat(SceneManager.getInstance().getActiveScene(), is(sameInstance(scene)));

        // and when switching second time to scene
        obtained = SceneManager.getInstance().setActiveScene(sceneName);

        // then
        assertThat(obtained, is(true));
        verify(scene, times(1)).show();
        verify(scene, times(0)).hide();
        assertThat(SceneManager.getInstance().getActiveScene(), is(sameInstance(scene)));
    }

    @Test
    public void setActiveScene() {

        // given
        String sceneNameOne = "Mock Scene 1";
        Scene sceneOne = mock(Scene.class);
        when(sceneOne.getName()).thenReturn(sceneNameOne);

        String sceneNameTwo = "Mock Scene 2";
        Scene sceneTwo = mock(Scene.class);
        when(sceneTwo.getName()).thenReturn(sceneNameTwo);

        SceneManager.getInstance().registerScene(sceneOne);
        SceneManager.getInstance().registerScene(sceneTwo);

        // when switching to first scene
        boolean obtained = SceneManager.getInstance().setActiveScene(sceneNameOne);

        // then
        assertThat(obtained, is(true));
        verify(sceneOne, times(1)).show();
        verify(sceneOne, times(0)).hide();
        verify(sceneTwo, times(0)).show();
        verify(sceneTwo, times(0)).hide();
        assertThat(SceneManager.getInstance().getActiveScene(), is(sameInstance(sceneOne)));

        // and when switching to second scene
        obtained = SceneManager.getInstance().setActiveScene(sceneNameTwo);

        // then
        assertThat(obtained, is(true));
        verify(sceneOne, times(1)).show();
        verify(sceneOne, times(1)).hide();
        verify(sceneTwo, times(1)).show();
        verify(sceneTwo, times(0)).hide();
        assertThat(SceneManager.getInstance().getActiveScene(), is(sameInstance(sceneTwo)));
    }

    @Test
    public void unregisterActiveSceneGivesFalse() {

        // given
        String sceneName = "Mock Scene";
        Scene scene = mock(Scene.class);
        when(scene.getName()).thenReturn(sceneName);

        SceneManager.getInstance().registerScene(scene);
        SceneManager.getInstance().setActiveScene(sceneName);

        // when
        boolean obtained = SceneManager.getInstance().unregisterScene(sceneName);

        // then
        assertThat(obtained, is(false));
    }

    @Test
    public void disposeDisposesAllRegisteredScenes() {

        // given
        String sceneNameOne = "Mock Scene 1";
        Scene sceneOne = mock(Scene.class);
        when(sceneOne.getName()).thenReturn(sceneNameOne);

        String sceneNameTwo = "Mock Scene 2";
        Scene sceneTwo = mock(Scene.class);
        when(sceneTwo.getName()).thenReturn(sceneNameTwo);

        SceneManager.getInstance().registerScene(sceneOne);
        SceneManager.getInstance().registerScene(sceneTwo);

        SceneManager.getInstance().setActiveScene(sceneNameOne);

        // when
        SceneManager.getInstance().dispose();

        // then
        assertThat(SceneManager.getInstance().getRegisteredScenes(), equalTo(Collections.<String>emptySet()));
        verify(sceneOne, times(1)).dispose();
        verify(sceneTwo, times(1)).dispose();
        assertThat(SceneManager.getInstance().getActiveScene(), is(not(nullValue())));
        assertThat(SceneManager.getInstance().getActiveScene(), is(not(sameInstance(sceneOne))));
        assertThat(SceneManager.getInstance().getActiveScene(), is(not(sameInstance(sceneTwo))));
    }
}
