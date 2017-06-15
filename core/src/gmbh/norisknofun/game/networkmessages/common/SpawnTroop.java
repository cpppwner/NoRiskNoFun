package gmbh.norisknofun.game.networkmessages.common;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by Katharina on 19.05.2017.
 */

public class SpawnTroop extends BasicMessageImpl {

    private static final long serialVersionUID = 1L;
    private  String playername;
    private String regionname;
    private int id;

    public SpawnTroop(String playername, String regionname) {
        this.playername = playername;
        this.regionname = regionname;
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public String getRegionname() {
        return regionname;
    }

    public void setRegionname(String regionname) {
        this.regionname = regionname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
