package gmbh.norisknofun.game;

import java.util.ArrayList;
import java.util.List;

import gmbh.norisknofun.assets.AssetMap;

/**
 * Class containing game related data.
 */
public class GameData {

    private AssetMap mapAsset = null;
    private int[] diceRoll;
    private String currentplayer;

    private List<Player> players = new ArrayList<>();

    public void setMapAsset(AssetMap mapAsset) {
        this.mapAsset = mapAsset;
    }

    public AssetMap getMapAsset() {
        if (mapAsset == null)
            throw new IllegalStateException("mapFile was not set");

        return mapAsset;
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public List<Player> getPlayers(){
        return players;
    }

    public void setDiceRoll(int[] roll) {
        diceRoll = roll;
    }
    public int[] getDiceRoll() {
        return diceRoll;
    }

    public void setCurrentplayer(String currentplayer){
        this.currentplayer=currentplayer;
    }
    public Player getCurrentplayer(){
        Player player= null;
        for(Player p: players){
            if(p.getPlayername().equals(currentplayer)){
                player=p;
            }
        }
        return player;
    }
}
