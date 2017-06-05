package gmbh.norisknofun.game.networkmessages;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

import java.io.Serializable;

/**
 * If Troops Left == No start Phase 1
 *
 * Start of Phase 1 check if somebody has won
 *
 * if not send also PlayerSpread
 *
 * Server -> Client
 */

public class EndGame extends BasicMessageImpl implements Serializable{

    private String winner;
    private boolean gameend;

    public EndGame(String winner, boolean gameend) {
        this.winner = winner;
        this.gameend = gameend;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public boolean isGameend() {
        return gameend;
    }

    public void setGameend(boolean gameend) {
        this.gameend = gameend;
    }
}
