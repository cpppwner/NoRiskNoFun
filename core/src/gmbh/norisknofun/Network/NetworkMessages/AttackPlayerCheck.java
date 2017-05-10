package gmbh.norisknofun.Network.NetworkMessages;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

/**
 *
 * GameServer checks if AttackedPlayer is reachable
 *
 * Server -> Client
 */


public class AttackPlayerCheck extends  BasicMessageImpl {

    String playername;
    String attackecPlayername;

    boolean attackreachable;


}
