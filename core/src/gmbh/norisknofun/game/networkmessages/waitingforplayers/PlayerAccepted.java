package gmbh.norisknofun.game.networkmessages.waitingforplayers;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

import gmbh.norisknofun.game.Player;

/**
 * If Player can Join then Server sends true otherwise PlayerRejected
 *
 * Server to Client
 */


public class PlayerAccepted extends PlayerJoined {

    private String playerId;
    private int playerColor;
    private int maxNumPlayers;
    private String mapName;

    public PlayerAccepted(Player player) {
        super(player.getPlayerName());
        playerId = player.getId();
        playerColor = player.getColor();
    }


    public String getPlayerId() {
        return playerId;
    }

    public int getPlayerColor() {
        return playerColor;
    }

    public int getMaxNumPlayers() {
        return maxNumPlayers;
    }

    public void setMaxNumPlayers(int maxNumPlayers) {
        this.maxNumPlayers = maxNumPlayers;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }
}
