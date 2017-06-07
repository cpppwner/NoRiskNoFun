package gmbh.norisknofun.game;

import gmbh.norisknofun.assets.AssetMap;

/**
 * Created by Peter on 04.06.2017.
 */

public class GameDataServer {

    private AssetMap mapAsset = null;
    private int maxPlayer;
    private int[] diceRoll;
    private String currentplayer;
    private Players players;

    public GameDataServer() {
        players= new Players();
    }

    public void setMapAsset(AssetMap mapAsset) {
        this.mapAsset = mapAsset;
    }

    public AssetMap getMapAsset() {
        if (mapAsset == null)
            throw new IllegalStateException("mapFile was not set");

        return mapAsset;
    }

    public void setMaxPlayer(int maxPlayer){
        this.maxPlayer=maxPlayer;
    }

    public int getMaxPlayer(){
        return maxPlayer;
    }
    public Players getPlayers() {
        return players;
    }

    public void setDiceRoll(int[] roll) {
        diceRoll = roll;
    }
    public int[] getDiceRoll() {
        return diceRoll;
    }

    public void setCurrentplayer(String currentPlayer){
        this.currentplayer=currentPlayer;
    }

    public Player getCurrentplayer(){
       return players.getPlayerByName(currentplayer);
    }

    public void setNextPlayer(){
        setCurrentplayer(players.getNextPlayername(currentplayer));
    }

}
