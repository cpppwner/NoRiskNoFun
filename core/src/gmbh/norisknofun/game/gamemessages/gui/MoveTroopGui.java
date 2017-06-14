package gmbh.norisknofun.game.gamemessages.gui;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 * Inform the Statemachine that a Region's troops have been updated
 */

public class MoveTroopGui extends BasicMessageImpl {
    private String fromRegion;


    private String toRegion;
    private int troopamount;
    private float x;
    private float y;


    public MoveTroopGui(String fromRegion, String toRegion, float x, float y) {
        this.fromRegion = fromRegion;
        this.toRegion = toRegion;
        this.x = x;
        this.y = y;
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

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}
