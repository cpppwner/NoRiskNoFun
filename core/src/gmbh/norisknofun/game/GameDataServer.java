package gmbh.norisknofun.game;

import com.badlogic.gdx.Gdx;

import java.io.InputStream;

import gmbh.norisknofun.assets.AssetLoaderFactory;
import gmbh.norisknofun.assets.impl.AssetLoaderFactoryImpl;
import gmbh.norisknofun.assets.impl.map.AssetMap;

/**
 * Created by Peter on 04.06.2017.
 */

public class GameDataServer {

    private String mapFilename = null;
    private AssetMap mapAsset = null;
    private int maxPlayer;
    private int[] diceRoll;
    private String currentplayer;
    private AssetLoaderFactory assetLoaderFactory;
    private Players players;

    public GameDataServer(){
        this.assetLoaderFactory = new AssetLoaderFactoryImpl();
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
            try (InputStream stream = Gdx.files.internal(mapFilename).read()) {
                mapAsset = assetLoaderFactory.createAssetLoaderMap().load(stream);
            } catch (Exception e) {
                Gdx.app.error("MAP", "Failed to load map asset", e);
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
