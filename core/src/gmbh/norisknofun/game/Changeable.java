package gmbh.norisknofun.game;

/**
 * Class to hold flags for the game scene
 */

public class Changeable <T> {

    /**
     * Indicate if something needs to be updated on the Scene
     */
    private boolean hasChanged = false;

    /**
     * Value containing information about the Scene updates
     * e.g. a GUI Message
     */
    private T currentValue;

    public Changeable() {

        this(null);
    }

    /**
     * Initialize changeable with initial value.
     *
     * <p>
     *     This will not set the changed flag.
     * </p>
     *
     * @param initialValue The initial value to use.
     */
    public Changeable(T initialValue) {
        currentValue = initialValue;
    }

    public void setValue(T newValue) {
        currentValue = newValue;
    }

    public T getValue() {
        return currentValue;
    }

    public boolean hasChanged() {
        return hasChanged;
    }

    public void setChanged() {
        hasChanged = true;
    }

    public void resetChanged() {
        hasChanged = false;
    }
}
