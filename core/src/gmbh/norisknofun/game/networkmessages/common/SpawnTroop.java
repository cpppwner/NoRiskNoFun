package gmbh.norisknofun.game.networkmessages.common;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by Katharina on 19.05.2017.
 */

public class SpawnTroop extends BasicMessageImpl {

    private static final long serialVersionUID = 1L;
    private String regionname;
    private String playername;
    private int id;

    public SpawnTroop(String regionname) {
        this.regionname = regionname;
        this.playername = "";
    }

    public SpawnTroop(String regionname, String playername) {
        this.regionname = regionname;
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

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }
}
