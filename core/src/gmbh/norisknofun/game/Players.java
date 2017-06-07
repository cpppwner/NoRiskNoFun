package gmbh.norisknofun.game;

import java.util.ArrayList;
import java.util.List;

import gmbh.norisknofun.game.Player;

/**
 * Created by pippp on 03.06.2017.
 */

public class Players {

    private List<Player> players= new ArrayList<>();


    public Players(){

    }
    public boolean addPlayer(Player player){
        // player with null name will not be added
        if (player.getPlayername() == null || player.getId() == null) {
            return false;
        }
        for(Player p: players){
            if(p.getPlayername().equals(player.getPlayername()) || p.getId().equals(player.getId())){
                return false;
            }
        }
        players.add(player);
        return true;
    }



    public boolean removePlayer(String playername){
        for(Player p: players){
            if(p.getPlayername().equals(playername)){
                players.remove(p);
                return true;
            }
        }
        return false;
    }
    public Player getPlayerByName(String name){

        for(Player p: players){
            if(p.getPlayername().equals(name)){
                return p;
            }
        }
        return null;
    }

    public Player getPlayerByID(String id){

        for(Player p: players){
            if(p.getId().equals(id)){
                return p;
            }
        }
        return null;
    }

    private int getPlayerIndexByName(String playername){
        int index=0;
        for(int i=0; i<players.size(); i++){
            if(players.get(i).getPlayername().equals(playername)){
                index=i;
                i=players.size();
            }
        }
        return index;
    }

    public String getNextPlayername(String playername){

        if (players.size() == 0 || playername == null) {
            return null;
        }

        int index = getPlayerIndexByName(playername);
        if(index>=players.size()-1){
            index=0;
        }else{
            index++;
        }

        return players.get(index).getPlayername();
    }
    public List<Player> getPlayers(){
        return players;
    }
}
