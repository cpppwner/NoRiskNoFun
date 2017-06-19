package gmbh.norisknofun.game.networkmessages.attack.evaluatedice;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Created by pippp on 15.05.2017.
 */

public class AttackResult extends BasicMessageImpl implements Serializable{
    private boolean won;
    private String attackerRegion;
    private int attackerTroops;
    private String defenderRegion;
    private String defenderRegionOwner;
    private int defenderTroops;
    private String winnerId;
    private String loserId;

    private static final long serialVersionUID = 1L;


    public AttackResult() {
        // fill via setter
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public String getAttackerRegion() {
        return attackerRegion;
    }

    public void setAttackerRegion(String attackerRegion) {
        this.attackerRegion = attackerRegion;
    }

    public int getAttackerTroops() {
        return attackerTroops;
    }

    public void setAttackerTroops(int attackerTroops) {
        this.attackerTroops = attackerTroops;
    }

    public String getDefenderRegion() {
        return defenderRegion;
    }

    public void setDefenderRegion(String defenderRegion) {
        this.defenderRegion = defenderRegion;
    }

    public String getDefenderRegionOwner() {
        return defenderRegionOwner;
    }

    public void setDefenderRegionOwner(String defenderRegionOwner) {
        this.defenderRegionOwner = defenderRegionOwner;
    }

    public int getDefenderTroops() {
        return defenderTroops;
    }

    public void setDefenderTroops(int defenderTroops) {
        this.defenderTroops = defenderTroops;
    }

    public String getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }

    public String getLoserId() {
        return loserId;
    }

    public void setLoserId(String loserId) {
        this.loserId = loserId;
    }
}
