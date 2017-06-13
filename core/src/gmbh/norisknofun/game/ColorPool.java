package gmbh.norisknofun.game;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;

/**
 * Created by philipp on 13.06.2017.
 */

public class ColorPool {


    private static ArrayList<PlayerColor> colorList;

    public ColorPool() {
        colorList = new ArrayList<PlayerColor>();
        colorList.add(new PlayerColor(Color.BLUE));
        colorList.add(new PlayerColor(Color.GREEN));
        colorList.add(new PlayerColor(Color.RED));
        colorList.add(new PlayerColor(Color.ORANGE));
        colorList.add(new PlayerColor(Color.YELLOW));
        colorList.add(new PlayerColor(Color.CYAN));
        colorList.add(new PlayerColor(Color.MAGENTA));

    }

    public Color getNextAvailableColor(){
        for(PlayerColor pc: colorList){
            if(pc.available){
                pc.available=false;
                return pc.color;
            }
        }

      return null;
    }

    public void relaseColor(Color color){
        for(PlayerColor pc: colorList){
            if(pc.color.equals(color)){
                pc.available=true;
            }
        }
    }

    public static ArrayList<PlayerColor> getColorList() {
        return colorList;
    }

}
