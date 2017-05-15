package gmbh.norisknofun.network.networkmessages;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

/**
 *
 * After Start Game,GameServer should choose which Player starts an then send this Message
 * Tell the Player he should Spread his troops
 *
 * Client -> Server
 */

public class PlayerSpreadFinished extends  BasicMessageImpl {

    String playername;
    // Region

}
