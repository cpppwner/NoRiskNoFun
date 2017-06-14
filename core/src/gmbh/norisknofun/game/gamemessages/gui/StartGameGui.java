package gmbh.norisknofun.game.gamemessages.gui;


import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * State will switch to SpreadTroopsState
 *
 *
 * Client (who created the Game)-> Server
 */
public class StartGameGui extends BasicMessageImpl {

    private static final long serialVersionUID = 1L;
    private boolean startGame = true;

    public StartGameGui(boolean startGame) {
        this.startGame = startGame;
    }

    public boolean isStartGame() {
        return startGame;
    }

    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }
}
