package gmbh.norisknofun.game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pippp on 03.06.2017.
 */

public class Players implements Serializable{

    private List<Player> playerlist = new ArrayList<>();



    public boolean addPlayer(Player player){
        // player with null name will not be added
        if (player.getPlayerName() == null || player.getId() == null) {
            return false;
        }
        for(Player p: playerlist){
            if(p.getPlayerName().equals(player.getPlayerName()) || p.getId().equals(player.getId())){
                return false;
            }
        }
        playerlist.add(player);
        return true;
    }



    public boolean removePlayer(String playername){
        for(Player p: playerlist){
            if(p.getPlayerName().equals(playername)){
                playerlist.remove(p);
                return true;
            }
        }
        return false;
    }
    public Player getPlayerByName(String name){

        for(Player p: playerlist){
            if(p.getPlayerName().equals(name)){
                return p;
            }
        }
        return null;
    }

    public Player getPlayerByID(String id){

        for(Player p: playerlist){
            if(p.getId().equals(id)){
                return p;
            }
        }
        return null;
    }

    private int getPlayerIndexByName(String playername){

        int index = -1;
        for(int i = 0; i< playerlist.size(); i++){
            if(playerlist.get(i).getPlayerName().equals(playername)){
                index = i;
                break;
            }
        }
        return index;
    }

    public String getNextPlayername(String playername){

        if (playerlist.isEmpty() || playername == null) {
            return null;
        }

        int index = getPlayerIndexByName(playername);
        if(index>= playerlist.size() - 1){
            index=0;
        }else{
            index++;
        }

        return playerlist.get(index).getPlayerName();
    }

    public int getNumPlayers() {
        return playerlist.size();
    }

    public List<Player> getPlayerlist(){
        return Collections.unmodifiableList(playerlist);
    }
}
