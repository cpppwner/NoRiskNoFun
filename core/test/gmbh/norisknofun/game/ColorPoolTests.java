package gmbh.norisknofun.game;

import com.badlogic.gdx.graphics.Color;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

/**
 * Test for testing {@link ColorPool}.
 */
public class ColorPoolTests {

    private ColorPool colorPool;

    @Before
    public void initialize() {
        colorPool = new ColorPool();
    }

    @Test
    public void aNewlyConstructedPoolHasNoUsedColors() {

        // when
        List<Color> obtained = colorPool.getUsedColors();

        // then
        assertThat(obtained, is(empty()));
    }

    @Test
    public void getNextAvailableColor() {

        for (int i = 0; i < ColorPool.ALL_AVAILABLE_COLORS.size(); i++) {

            Color obtained = colorPool.getNextAvailableColor();
            assertThat(obtained, is(ColorPool.ALL_AVAILABLE_COLORS.get(i)));
        }

        assertThat(colorPool.getUsedColors(), is(equalTo(ColorPool.ALL_AVAILABLE_COLORS)));
    }

    @Test
    public void getNextAvailableColorFailed() {

        for(int i = 0; i < ColorPool.ALL_AVAILABLE_COLORS.size(); i++) {
            colorPool.getNextAvailableColor(); // use up all colors
        }

        assertThat(colorPool.getUsedColors(), is(equalTo(ColorPool.ALL_AVAILABLE_COLORS)));

        Color color = colorPool.getNextAvailableColor();
        assertNull(color);
    }

    @Test
    public void releaseNullColor() {

        for(int i = 0; i < ColorPool.ALL_AVAILABLE_COLORS.size(); i++) {
            colorPool.getNextAvailableColor(); // use up all colors
        }

        assertThat(colorPool.getUsedColors(), is(equalTo(ColorPool.ALL_AVAILABLE_COLORS)));

        colorPool.releaseColor(null);

        assertThat(colorPool.getUsedColors(), is(equalTo(ColorPool.ALL_AVAILABLE_COLORS)));
    }

    @Test
    public void releaseColorThatIsNotInUse() {

        for(int i = 0; i < ColorPool.ALL_AVAILABLE_COLORS.size(); i++) {
            colorPool.releaseColor(ColorPool.ALL_AVAILABLE_COLORS.get(i));
        }

        assertThat(colorPool.getUsedColors(), is(empty()));

    }

    @Test
    public void multipleGetAndRelease() {

        for(int i = 0; i < ColorPool.ALL_AVAILABLE_COLORS.size(); i++) {
            colorPool.getNextAvailableColor(); // use up all colors
        }

        assertThat(colorPool.getUsedColors(), is(equalTo(ColorPool.ALL_AVAILABLE_COLORS)));

        for(int i = 0; i < ColorPool.ALL_AVAILABLE_COLORS.size(); i++) {
            colorPool.releaseColor(ColorPool.ALL_AVAILABLE_COLORS.get(i));
        }

        assertThat(colorPool.getUsedColors(), is(empty()));

        for(int i = 0; i < ColorPool.ALL_AVAILABLE_COLORS.size(); i++) {
            colorPool.getNextAvailableColor(); // use up all colors
        }

        assertThat(colorPool.getUsedColors(), is(equalTo(ColorPool.ALL_AVAILABLE_COLORS)));

        for(int i = 0; i < ColorPool.ALL_AVAILABLE_COLORS.size(); i++) {
            colorPool.releaseColor(ColorPool.ALL_AVAILABLE_COLORS.get(i));
        }

        assertThat(colorPool.getUsedColors(), is(empty()));

    }




}
