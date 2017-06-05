package gmbh.norisknofun.game.networkmessages.waitingforplayers;


/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */


import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * After all Playersjoined or if he wants :D
 *
 * State = Spiel Gestarted
 *
 *
 * Client (who created the Game)-> Server
 */
public class StartGame extends BasicMessageImpl implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean startGame = true;

    public StartGame(boolean startGame) {
        this.startGame = startGame;
    }

    public boolean isStartGame() {
        return startGame;
    }

    public void setStartGame(boolean startGame) {
        this.startGame = startGame;
    }
}
