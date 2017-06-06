package gmbh.norisknofun.game;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.assets.AssetFactory;
import gmbh.norisknofun.assets.AssetMap;

/**
 * Created by Peter on 04.06.2017.
 */

public class GameDataServer {

    private String mapFilename = null;
    private AssetMap mapAsset = null;
    private int maxPlayer;
    private int[] diceRoll;
    private String currentplayer;
    private AssetFactory assetFactory;
    private Players players;

    public GameDataServer(AssetFactory assetFactory){
        this.assetFactory = assetFactory;
        players= new Players();
    }


    public void setMapFile(String mapFilename) {
        this.mapFilename = mapFilename;
        mapAsset = null;
    }

    public AssetMap getMapAsset() {
        if (mapFilename == null)
            throw new IllegalStateException("mapFile was not set");

        if (mapAsset == null) {
            mapAsset = assetFactory.createAssetMap(mapFilename);
            if (mapAsset == null) {
                Gdx.app.error("MAP", "Failed to load map asset");
            }
        }

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
