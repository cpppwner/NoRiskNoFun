package gmbh.norisknofun.Network.NetworkMessages;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

/**
 * If Troops Left == No start Phase 1
 *
 * Start of Phase 1 check if somebody has won
 *
 * if not send also PlayerSpread
 *
 * Server -> Client
 */

public class EndGame extends BasicMessageImpl {

    String winner;
    boolean gameend;
}
