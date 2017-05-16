package gmbh.norisknofun.game.networkmessages.waitingforplayers;


/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */


import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * After all Playersjoined or if he wants :D
 *
 * State = Spiel Gestarted
 *
 *
 * Client (who created the Game)-> Server
 */
public class StartGame extends BasicMessageImpl {
    boolean startGame = true;

}
