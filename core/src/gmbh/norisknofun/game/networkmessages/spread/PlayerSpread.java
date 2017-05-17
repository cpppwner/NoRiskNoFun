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
 * Server -> Client
 */


public class PlayerSpread extends BasicMessageImpl {

    public String playername;
    public boolean playersTurn = true;

}
