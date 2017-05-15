package gmbh.norisknofun.Network.NetworkMessages;

import java.io.Serializable;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */


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
