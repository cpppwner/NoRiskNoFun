package gmbh.norisknofun.game;

import com.badlogic.gdx.graphics.Color;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

/**
 * Created by Philipp on 13.06.17.
 */

public class ColorPoolTest {

    ColorPool colorPool;

    @Before
    public void initialize() {
        colorPool = new ColorPool();

    }

    @Test
    public void getNextAvailableColor() {

        Color color = colorPool.getNextAvailableColor();
        for(PlayerColor c: colorPool.getColorList()) {
            if (c.color.equals(color)) {
                assertEquals(c.available, false);
            }
        }
    }

    @Test
    public void getNextAvailableColorFailed() {
        for(int i=0;i<colorPool.getColorList().size();i++) {
            colorPool.getNextAvailableColor();
        }

        Color color=colorPool.getNextAvailableColor();
        assertNull(color);
    }

    @Test
    public void relaseColor() {

        Color color = colorPool.getNextAvailableColor();
        colorPool.relaseColor(color);
        for(PlayerColor c: colorPool.getColorList()) {
            if (c.color.equals(color)) {
                assertEquals(c.available, true);
            }
        }
    }
    @Test
    public void relaseColorgetNext() {

        Color color = colorPool.getNextAvailableColor();
        colorPool.relaseColor(color);
        for (PlayerColor c : colorPool.getColorList()) {
            if (c.color.equals(color)) {
                assertEquals(c.available, true);
            }
        }
        Color color2 = colorPool.getNextAvailableColor();
        for (PlayerColor c : colorPool.getColorList()) {
            if (c.color.equals(color2)) {
                assertEquals(c.available, false);
            }
        }
    }


    @Test
    public void getNextAvailableRandom() {

        colorPool.getNextAvailableColor();
        colorPool.getNextAvailableColor();
        Color color =colorPool.getNextAvailableColor();
        colorPool.getNextAvailableColor();
        colorPool.relaseColor(color);
        colorPool.getNextAvailableColor();

        int cnt=0;
        for (PlayerColor c : colorPool.getColorList()) {

            if (!c.available) {
                cnt++;
            }
        }
        assertEquals(cnt,4);


    }




}
