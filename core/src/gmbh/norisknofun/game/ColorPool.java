package gmbh.norisknofun.game;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Color pool for getting available colors.
 */
public class ColorPool {

    /**
     * All available colors for a color pool.
     */
    static final List<Color> ALL_AVAILABLE_COLORS = Arrays.asList(
            Color.BLUE,
            Color.GREEN,
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            Color.CYAN,
            Color.MAGENTA
    );

    /**
     * The colors that are currently in use.
     */
    private final List<Color> usedColors = new ArrayList<>(ALL_AVAILABLE_COLORS.size());

    /**
     * Get next available color from the pool.
     */
    public Color getNextAvailableColor(){

        Color nextAvailable = null;
        for (Color color : ALL_AVAILABLE_COLORS) {
            if (!usedColors.contains(color)) {
                nextAvailable = color;
                break;
            }
        }

        if (nextAvailable != null) {
            usedColors.add(nextAvailable);
        }

        return nextAvailable;
    }

    /**
     * Release used color and return it into the pool of available color.
     */
    public void releaseColor(Color color) {

        usedColors.remove(color);
    }

    /**
     * Method to get all used colors.
     *
     * @return An umodifiable list containing all colors so far in use.
     */
    List<Color> getUsedColors() {
        return Collections.unmodifiableList(usedColors);
    }
}
