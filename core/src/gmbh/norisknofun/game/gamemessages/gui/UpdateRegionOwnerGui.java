package gmbh.norisknofun.game.gamemessages.gui;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

public class UpdateRegionOwnerGui extends BasicMessageImpl{
    private String regionName;
    private String newOwner;

    public UpdateRegionOwnerGui(String regionName, String newOwner) {
        this.regionName = regionName;
        this.newOwner = newOwner;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getNewOwner() {
        return newOwner;
    }

    public void setNewOwner(String newOwner) {
        this.newOwner = newOwner;
    }
}
