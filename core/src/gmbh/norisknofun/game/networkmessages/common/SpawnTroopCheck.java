package gmbh.norisknofun.game.networkmessages.common;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by Katharina on 19.05.2017.
 */

public class SpawnTroopCheck extends BasicMessageImpl implements Serializable{


    private static final long serialVersionUID = 1L;
    private boolean canspawn=false;
    private String errormessage="";

    public SpawnTroopCheck(boolean canspawn, String errormessage) {
        this.canspawn = canspawn;
        this.errormessage = errormessage;
    }

    public boolean isCanspawn() {
        return canspawn;
    }

    public void setCanspawn(boolean canspawn) {
        this.canspawn = canspawn;
    }

    public String getErrormessage() {
        return errormessage;
    }

    public void setErrormessage(String errormessage) {
        this.errormessage = errormessage;
    }
}
