package gmbh.norisknofun.game.networkmessages;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */


/**
 * After Gamemove -> NextPlayer
 *
 * State switches to Phase 1
 *
 * Server -> Client
 */

public class NextPlayer extends BasicMessageImpl {

    String playername;
    boolean playersTurn = true;



}
