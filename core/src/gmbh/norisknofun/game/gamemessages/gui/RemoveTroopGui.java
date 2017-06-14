package gmbh.norisknofun.game.gamemessages.gui;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;


/**
 * Message indicating that the GUI should remove some troops
 */
public class RemoveTroopGui extends BasicMessageImpl {

    private String regionName;
    private int troopAmount;

    public RemoveTroopGui(String regionName, int troopAmount) {
        this.regionName = regionName;
        this.troopAmount = troopAmount;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public int getTroopAmount() {
        return troopAmount;
    }

    public void setTroopAmount(int troopAmount) {
        this.troopAmount = troopAmount;
    }

}
