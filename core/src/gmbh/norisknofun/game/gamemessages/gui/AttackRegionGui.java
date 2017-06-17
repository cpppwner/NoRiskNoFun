package gmbh.norisknofun.game.gamemessages.gui;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 17.06.2017.
 */

public class AttackRegionGui extends BasicMessageImpl {

    private String originRegion;
    private String attackedRegion;

    public AttackRegionGui(String originRegion, String destinationRegion){
        this.originRegion=originRegion;
        this.attackedRegion =destinationRegion;
    }

    public String getOriginRegion() {
        return originRegion;
    }

    public void setOriginRegion(String originRegion) {
        this.originRegion = originRegion;
    }

    public String getAttackedRegion() {
        return attackedRegion;
    }

    public void setAttackedRegion(String attackedRegion) {
        this.attackedRegion = attackedRegion;
    }
}
