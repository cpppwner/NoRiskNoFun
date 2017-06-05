package gmbh.norisknofun.game.networkmessages.waitingforplayers;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

/**
 * If Player can Join then Server sends true otherwise false
 *
 * Server -> Client
 */


public class PlayerJoinedCheck extends PlayerJoined {

    private  boolean allowedtojoin;
    private String senderId;

    public PlayerJoinedCheck(String playername, String senderId) {
        super(playername);
        this.senderId=senderId;

    } public PlayerJoinedCheck(String playername) {
        super(playername);

    }

    public boolean isAllowedtojoin() {
        return allowedtojoin;
    }

    public void setAllowedtojoin(boolean allowedtojoin) {
        this.allowedtojoin = allowedtojoin;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
