package gmbh.norisknofun.game;

import gmbh.norisknofun.network.Session;

/**
 * Created by pippp on 17.05.2017.
 */

public class Player {

    private  String playername="";
    private boolean ishost=false;
    private int troopToSpread =0;

    private Session session=null;


    public Player(Session session){
        this.session=session;
    }

    public Session getSession(){
        return session;
    }
    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
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
}
