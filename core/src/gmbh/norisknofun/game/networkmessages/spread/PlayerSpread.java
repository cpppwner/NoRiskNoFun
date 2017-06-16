package gmbh.norisknofun.game.networkmessages.spread;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 *
 * After Start Game,GameServer should choose which Player starts an then send this Message
 * or after others Spread was finished choose next Player to spread
 * Tell the Player he should Spread his troops
 *
 * Server to Client
 */


public class PlayerSpread extends BasicMessageImpl {

    private  String playername;



    private  boolean playersTurn = true;

    public PlayerSpread(String playername, boolean playersTurn) {
        this.playername = playername;
        this.playersTurn = playersTurn;
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public boolean isPlayersTurn() {
        return playersTurn;
    }

    public void setPlayersTurn(boolean playersTurn) {
        this.playersTurn = playersTurn;
    }
}
