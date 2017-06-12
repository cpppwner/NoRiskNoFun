package gmbh.norisknofun.game;

/**
 * Class to hold flags for the game scene
 */

public class Changeable <T>{

    /**
     * Indicate if something needs to be updated on the Scene
     */
    private boolean hasChanged;

    /**
     * Value containing information about the Scene updates
     * e.g. a GUI Message
     */
    private T currentValue;

    public void setMessage(T newValue) {
        currentValue = newValue;
    }

    public T getMessage() {
        return currentValue;
    }

    public boolean getChanged() {
        return hasChanged;
    }

    public void setChanged(boolean hasChanged) {
        this.hasChanged = hasChanged;
    }
}
