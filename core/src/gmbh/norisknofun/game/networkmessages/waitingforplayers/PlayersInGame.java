package gmbh.norisknofun.game.networkmessages.waitingforplayers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gmbh.norisknofun.game.Players;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Message sent from server to client, when players where added/removed.
 */
public class PlayersInGame extends BasicMessageImpl implements Serializable {

    private static final long serialVersionUID = 1L;

    private final List<Player> allPlayers;

    public PlayersInGame(Players players) {

        allPlayers = new ArrayList<>(players.getNumPlayers());
        initializeAllPlayers(players);
    }

    private void initializeAllPlayers(Players players) {

        for (gmbh.norisknofun.game.Player player : players.getPlayerlist()) {
            allPlayers.add(new Player(player.getPlayerName(), player.getColor()));
        }
    }

    public List<Player> getAllPlayers() {
        return Collections.unmodifiableList(allPlayers);
    }

    public static class Player implements Serializable {

        private static final long serialVersionUID = 1L;

        private final String name;
        private final int color;

        private Player(String name, int color) {
            this.name = name;
            this.color = color;
        }

        public String getName() {
            return name;
        }

        public int getColor() {
            return color;
        }
    }
}
