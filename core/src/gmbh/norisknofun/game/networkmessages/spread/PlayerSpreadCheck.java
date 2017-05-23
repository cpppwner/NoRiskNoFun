package gmbh.norisknofun.game.networkmessages.spread;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */


import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * If Spread of Player was correct tell him
 *
 * If check is false Player has to spread again
 * Server -> Client
 */

public class PlayerSpreadCheck extends BasicMessageImpl {

    public String playername;
    public boolean check;

    public PlayerSpreadCheck(String playername, boolean check) {
        this.playername = playername;
        this.check = check;
    }
    //region

}
