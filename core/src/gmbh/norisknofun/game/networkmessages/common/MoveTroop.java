package gmbh.norisknofun.game.networkmessages.common;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;

/**
 *
 * After moved a Troops
 *
 * Client -> Server
 */


public class MoveTroop extends BasicMessageImpl implements Serializable{

    private static final long serialVersionUID = 1L;
    private String  playername;
    private int     troopamount;
    private String toRegion;
    private String fromRegion;
    private int figureId;

    public MoveTroop(String playername, int troopamount,  String fromRegion,String toRegion, int id) {
        this.playername = playername;
        this.troopamount = troopamount;
        this.toRegion = toRegion;
        this.fromRegion = fromRegion;
        this.figureId=id;
    }
    public MoveTroop(){
        //to fill the message object via setter
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
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

    public String getFromRegion() {
        return fromRegion;
    }

    public void setFromRegion(String fromRegion) {
        this.fromRegion = fromRegion;
    }

    public int getFigureId() {
        return figureId;
    }

    public void setFigureId(int figureId) {
        this.figureId = figureId;
    }
}
