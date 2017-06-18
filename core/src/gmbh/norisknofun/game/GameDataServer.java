package gmbh.norisknofun.game;

import gmbh.norisknofun.assets.AssetMap;

/**
 * Container class storing information required by the server.
 */
public class GameDataServer {

    private String mapFilename = null;
    private AssetMap mapAsset = null;
    private int maxPlayer;
    private String currentplayer;
    private AssetMap.Region attackerRegion;
    private AssetMap.Region defendersRegion;
    private int attackingTroops;
    private int[] attackerDiceResult= new int[3];
    private int[] defenderDiceResult = new int[3];
    private Players players;
    private int currentFigureId = 0;

    public GameDataServer() {
        players= new Players();
    }

    public void setMapFilename(String mapFilename) {
        this.mapFilename = mapFilename;
    }

    public String getMapFilename() {
        return  mapFilename;
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

    public void setCurrentplayer(String currentPlayer){
        this.currentplayer=currentPlayer;
    }

    public Player getCurrentplayer(){
       return players.getPlayerByName(currentplayer);
    }

    public void setNextPlayer(){
        setCurrentplayer(players.getNextPlayername(currentplayer));
    }

    public AssetMap.Region getAttackerRegion() {
        return attackerRegion;
    }

    public void setAttackerRegion(AssetMap.Region attackerRegion) {
        this.attackerRegion = attackerRegion;
    }

    public AssetMap.Region getDefendersRegion() {
        return defendersRegion;
    }

    public void setDefendersRegion(AssetMap.Region defendersRegion) {
        this.defendersRegion = defendersRegion;
    }

    public AssetMap.Region getRegionByName(String name){
        return mapAsset.getRegion(name);
    }

    public int getAttackingTroops() {
        return attackingTroops;
    }

    public void setAttackingTroops(int attackingTroops) {
        this.attackingTroops = attackingTroops;
    }

    public Player getPlayerByName(String name){
        return players.getPlayerByName(name);
    }

    public Player getPlayerById(String senderId) {return players.getPlayerByID(senderId);}

    public int getNumPlayers() {
        return players.getNumPlayers();
    }

    public int[] getAttackerDiceResult() {
        return attackerDiceResult;
    }

    public void setAttackerDiceResult(int[] attackerDiceResult) {
        this.attackerDiceResult = attackerDiceResult;
    }

    public int[] getDefenderDiceResult() {
        return defenderDiceResult;
    }

    public void setDefenderDiceResult(int[] defenderDiceResult) {
        this.defenderDiceResult = defenderDiceResult;
    }

    public Players getPlayers(){
        return players;
    }
    public boolean defenderhasDiceResult(){
        for(int i=0; i<defenderDiceResult.length; i++){
        if(defenderDiceResult[i]==0)
            return false;
        }
        return true;
    }

    public boolean attackerhasDiceResult(){
        for(int i=0; i<defenderDiceResult.length; i++){
            if(defenderDiceResult[i]==0)
                return false;
        }
        return true;
    }

    public void clearAttackData(){
        attackerDiceResult= new int[3];
        defenderDiceResult= new int[3];
        attackingTroops=0;
        defendersRegion=null;
        attackerRegion=null;
    }
    public boolean isServerFull() {
        return getNumPlayers() == getMaxPlayer();
    }

    public int getCurrentFigureId() {
        return currentFigureId;
    }

    public void setCurrentFigureId(int currentFigureId) {
        this.currentFigureId = currentFigureId;
    }

    public int nextFigureId() {
        return currentFigureId++;
    }

    public int getNumberOfRegionOwnedByPlayer(String playername){
        int count=0;

        for (AssetMap.Region region: mapAsset.getRegions()) {
            if(region.getOwner().equals(playername)){
                count++;
            }
        }
        return count;
    }


}
