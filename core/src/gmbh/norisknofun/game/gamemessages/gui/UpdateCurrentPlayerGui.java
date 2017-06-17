package gmbh.norisknofun.game.gamemessages.gui;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Game message to signal the GUI that the current player should be updated
 * This should remove race conditions as it is handled in the render thread
 * and not directly by the state machine
 */

public class UpdateCurrentPlayerGui extends BasicMessageImpl {
    private String currentPlayer;

    public UpdateCurrentPlayerGui(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

}
