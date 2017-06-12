package gmbh.norisknofun.game;

import java.util.ArrayList;
import java.util.List;

import gmbh.norisknofun.assets.AssetMap;
import gmbh.norisknofun.game.networkmessages.Message;

/**
 * Class containing game related data.
 */
public class GameData {

    private Changeable<Message> guiChanges = new Changeable<>();
    private AssetMap mapAsset = null;
    private int[] diceRoll;
    private String currentplayer;

    private List<Player> players = new ArrayList<>();

    public void setMapAsset(AssetMap mapAsset) {
        this.mapAsset = mapAsset;
    }

    public AssetMap getMapAsset() {
        if (mapAsset == null)
            throw new IllegalStateException("mapFile was not set");

        return mapAsset;
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public List<Player> getPlayers(){
        return players;
    }

    public void setDiceRoll(int[] roll) {
        diceRoll = roll;
    }
    public int[] getDiceRoll() {
        return diceRoll;
    }

    public void setCurrentplayer(String currentplayer){
        this.currentplayer=currentplayer;
    }
    public Player getCurrentplayer(){
        Player player= null;
        for(Player p: players){
            if(p.getPlayername().equals(currentplayer)){
                player=p;
            }
        }
        return player;
    }

    /**
     * Modify the action the GUI should perform and automatically set the changed flag
     * @param message Action the GUI should perform
     */
    public void setGuiChanges(Message message) {
        guiChanges.setMessage(message);
        guiChanges.setChanged(true);

    }

    /**
     * Get the action the GUI should perform
     * @return GUI Message
     */
    public Message getGuiChanges() {
        return guiChanges.getMessage();
    }

    /**
     * Set if the GUI should perform an action
     * @param changedFlag Flag signalling the GUI to perform an action
     */
    public void setChangedFlag(boolean changedFlag) {
        guiChanges.setChanged(changedFlag);
    }

    /**
     * Check if there are any GUI actions to perform
     * This will be called by the render thread
     */
    public boolean hasChanged() {
        return guiChanges.getChanged();
    }
}
