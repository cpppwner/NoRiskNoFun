package gmbh.norisknofun.game;


/**
 * Created by pippp on 17.05.2017.
 */

public class Player {

    public static final String NULL_PLAYERNAME = "none";

    private String playername="";
    private boolean ishost=false;
    private int troopToSpread =0;
    private String id = "";
    private int playerColor;
    private int troopsActive; // current amount of troops this player owns


    public Player(){
        // for testing
    }

    public Player(String playername, String id,int playerColor){
        this.playername=playername;
        this.id=id;
        this.playerColor = playerColor;
    }

    public Player(String playername, String id){
        this.playername=playername;
        this.id=id;

    }

    public String getPlayerName() {
        return playername;
    }

    public void setPlayerName(String playername) {
        this.playername = playername;
    }

    public boolean ishost() {
        return ishost;
    }

    public void setIshost(boolean ishost) {
        this.ishost = ishost;
    }

    public int getTroopToSpread() {
        return troopToSpread;
    }

    public void setTroopToSpread(int troopToSpread) {
        this.troopToSpread = troopToSpread;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setColor(int color) {
        this.playerColor = color;
    }

    public int getColor() {
        return playerColor;
    }

    public int getTroopsActive() {
        return troopsActive;
    }

    public void setTroopsActive(int troopsActive) {
        this.troopsActive = troopsActive;
    }

    public void updateTroopsActive(int troopsActive) {
        this.troopsActive += troopsActive;
    }
}
