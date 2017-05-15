package gmbh.norisknofun.Network.NetworkMessages;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

/**
 *
 * If Spread is finished player can Attack
 *
 * Client -> Server
 */


public class AttackPlayer extends  BasicMessageImpl {

    String playername;
    String attackecPlayername;

    boolean attack;


}
