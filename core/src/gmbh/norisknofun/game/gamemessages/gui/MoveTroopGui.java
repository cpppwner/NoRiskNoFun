package gmbh.norisknofun.game.gamemessages.gui;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Inform the Statemachine that a Region's troops have been updated
 */

public class MoveTroopGui extends BasicMessageImpl {
    private String fromRegion;


    private String toRegion;
    private int troopamount;
    private int figureId;


    public MoveTroopGui(String fromRegion, String toRegion, int id) {
        this.fromRegion = fromRegion;
        this.toRegion = toRegion;
        this.figureId=id;

    }

    public String getFromRegion() {
        return fromRegion;
    }

    public void setFromRegion(String fromRegion) {
        this.fromRegion = fromRegion;
    }

    public int getTroopamount() {
        return troopamount;
    }

    public void setTroopamount(int troopamount) {
        this.troopamount = troopamount;
    }

    public String getToRegion() {
        return toRegion;
    }

    public void setToRegion(String toRegion) {
        this.toRegion = toRegion;
    }

    public int getFigureId() {
        return figureId;
    }

    public void setFigureId(int figureIds) {
        this.figureId = figureIds;
    }
}
