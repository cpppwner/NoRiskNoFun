package gmbh.norisknofun.Network.NetworkMessages;

import java.io.Serializable;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

/**
 * If Player can Join then Server sends true otherwise false
 *
 * Server -> Client
 */


public class PlayerJoinedCheck extends PlayerJoined {

    boolean allowedtojoin;


}
