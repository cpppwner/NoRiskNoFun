package gmbh.norisknofun.assets;

import com.badlogic.gdx.graphics.Color;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for testing the {@link FontDescriptor}.
 */
public class FontDescriptorTests {

    private static final String FONT_FILENAME = "fonts/Arial.ttf";
    private static final int FONT_SIZE = 36;
    private static final Color BORDER_COLOR = Color.RED;
    private static final float BORDER_WIDTH = 2.0f;
    private static final Color FONT_COLOR = Color.GREEN;

    private FontDescriptor.Builder fontDescriptorBuilder;

    @Before
    public void setUp() {

        fontDescriptorBuilder = new FontDescriptor.Builder()
                .setFontFilename(FONT_FILENAME)
                .setFontSize(FONT_SIZE)
                .setBorderColor(BORDER_COLOR)
                .setBorderWidth(BORDER_WIDTH)
                .setForegroundColor(FONT_COLOR)
                .setRoundBorder();
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void defaultFontDescriptor() {

        // given
        FontDescriptor target = fontDescriptorBuilder.build();

        // then
        assertThat(target.getFontFilename(), is(FONT_FILENAME));
        assertThat(target.getFontSize(), is(FONT_SIZE));
        assertThat(target.getBorderColor(), is(BORDER_COLOR));
        assertThat(target.getBorderWidth(), is(BORDER_WIDTH));
        assertThat(target.getForegroundColor(), is(FONT_COLOR));
        assertThat(target.isStraightBorderUsed(), is(false));
    }

    @Test
    public void whenUsingStraightBorder() {

        // given
        FontDescriptor target = fontDescriptorBuilder.setStraigtBorder().build();

        // then
        assertThat(target.getFontFilename(), is(FONT_FILENAME));
        assertThat(target.getFontSize(), is(FONT_SIZE));
        assertThat(target.getBorderColor(), is(BORDER_COLOR));
        assertThat(target.getBorderWidth(), is(BORDER_WIDTH));
        assertThat(target.getForegroundColor(), is(FONT_COLOR));
        assertThat(target.isStraightBorderUsed(), is(true));
    }

    @Test
    public void whenFontFilenameIsNull() {

        // given
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("fontFilename is null or empty");

        // when/then
        fontDescriptorBuilder.setFontFilename(null).build();
    }

    @Test
    public void whenFontFilenameIsAnEmptyString() {

        // given
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("fontFilename is null or empty");

        // when/then
        fontDescriptorBuilder.setFontFilename("").build();
    }

    @Test
    public void whenFontSizeIsLessThanZero() {

        // given
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("fontSize is less than or equal to 0");

        // when/then
        fontDescriptorBuilder.setFontSize(-1).build();
    }

    @Test
    public void whenFontSizeIsEqualToZero() {

        // given
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("fontSize is less than or equal to 0");

        // when/then
        fontDescriptorBuilder.setFontSize(0).build();
    }

    @Test
    public void whenBorderWidthIsLessThanZero() {

        // given
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("borderWidth is less than 0");

        // when/then
        fontDescriptorBuilder.setBorderWidth(-1).build();
    }

    @Test
    public void whenForegroundColorIsNull() {

        // given
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("foregroundColor is null");

        // when/then
        fontDescriptorBuilder.setForegroundColor(null).build();
    }

    @Test
    public void whenBorderColorIsNull() {

        // given
        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage("borderColor is null");

        // when/then
        fontDescriptorBuilder.setBorderColor(null).build();
    }
}
