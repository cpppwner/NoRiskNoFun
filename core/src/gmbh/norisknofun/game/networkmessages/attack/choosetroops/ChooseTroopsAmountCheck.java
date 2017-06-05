package gmbh.norisknofun.game.networkmessages.attack.choosetroops;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 15.05.2017.
 */

public class ChooseTroopsAmountCheck extends BasicMessageImpl implements Serializable {

    private boolean check;
    private static final long serialVersionUID = 1L;

    public ChooseTroopsAmountCheck(boolean check) {
        this.check = check;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
