package gmbh.norisknofun.game;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import gmbh.norisknofun.assets.AssetMap;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayersInGame;
import gmbh.norisknofun.scene.SceneData;

/**
 * Class containing game related data (client side).
 */
public class GameData {


    /**
     * Helper field to pass around some last error message to the GUI.
     *
     * <p>
     *     The main menu will check if this has been changed, and if so display it.
     * </p>
     */
    private final Changeable<String> lastError = new Changeable<>();
    private final Changeable<AssetMap> mapAsset = new Changeable<>(null);
    private Changeable<Queue<Message>> guiChanges = new Changeable<>();

    private final Player myself = new Player();
    private Player currentPlayer = null;
    private final Changeable<List<Player>> allPlayers = new Changeable<>();

    private int[] diceRoll;
    private int availableDice;
    private int maxNumPlayers;
    private String mapFilename;

    public GameData() {
        allPlayers.setValue(new LinkedList<Player>());
        guiChanges.setValue(new ConcurrentLinkedQueue<Message>());
    }

    public void setMapAsset(AssetMap mapAsset) {
        this.mapAsset.setValue(mapAsset);
        this.mapAsset.setChanged();
    }

    public AssetMap getMapAsset() {
        if (mapAsset.getValue() == null)
            throw new IllegalStateException("mapAsset was not set");

        return mapAsset.getValue();
    }

    public void setDiceRoll(int[] roll) {
        diceRoll = roll;
    }
    public int[] getDiceRoll() {
        return diceRoll;
    }

    /**
     * Modify the action the GUI should perform and automatically set the changed flag
     * @param message Action the GUI should perform
     */
    public void setGuiChanges(Message message) {
        guiChanges.getValue().offer(message);
        guiChanges.setChanged();
    }

    /**
     * Get the action the GUI should perform
     * @return GUI Message
     */
    public Queue<Message> getGuiChanges() {
        return guiChanges.getValue();
    }

    /**
     * Set if the GUI should perform an action
     * @param changedFlag Flag signalling the GUI to perform an action
     */
    public void setChangedFlag(boolean changedFlag) {

        if (changedFlag)
            guiChanges.setChanged();
        else
            guiChanges.resetChanged();
    }

    /**
     * Check if there are any GUI actions to perform
     * This will be called by the render thread
     */
    public boolean hasChanged() {
        return guiChanges.hasChanged();
    }

    public void setPlayerName(String playerName) {
        getMyself().setPlayerName(playerName);
    }

    public String getPlayerName() {
        return getMyself().getPlayerName();
    }

    public Player getMyself() {
        return myself;
    }

    /**
     * Set the last error that occurred.
     *
     * @param lastError The last error messaged.
     */
    public void setLastError(String lastError) {
        this.lastError.setValue(lastError);
        this.lastError.setChanged();
    }

    /**
     * Get the last error message, if one was set before using {@link SceneData#setLastError(String)}.
     *
     * @return The last error message that was set or {@code null} if none was set.
     */
    public String getLastError() {

        String result = null;

        if (lastError.hasChanged()) {
            lastError.resetChanged();
            result = lastError.getValue();
        }

        return result;
    }

    public void setCurrentPlayer(String currentPlayer) {

        Player newCurrentPlayer = null;

        for (Player player : allPlayers.getValue()) {
            if (player.getPlayerName().equals(currentPlayer)) {
                newCurrentPlayer = player;
                break;
            }
        }

        this.currentPlayer = newCurrentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void addPlayer(Player player) {

        allPlayers.getValue().add(player);
        allPlayers.setChanged();
    }

    public void updateAllPlayers(List<PlayersInGame.Player> players) {

        allPlayers.getValue().clear();
        for (PlayersInGame.Player player : players) {
            addPlayer(new Player(player.getName(), "", player.getColor()));
        }
    }

    public boolean hasPlayersChanged() {
        return allPlayers.hasChanged();
    }

    public void resetAllPlayersChanged() {

        allPlayers.resetChanged();
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(allPlayers.getValue());
    }

    public void setMapFilename(String mapFilename) {
        this.mapFilename = mapFilename;
    }

    public String getMapFilename() {
        return mapFilename;
    }


    public void setMaxNumPlayers(int maxNumPlayers) {
        this.maxNumPlayers = maxNumPlayers;
    }

    public int getMaxNumPlayers() {
        return maxNumPlayers;
    }

    public int getAvailableDice() {
        return availableDice;
    }

    public boolean isMyTurn(){
        return myself.getPlayerName().equals(currentPlayer.getPlayerName());
    }

    public void setAvailableDice(int availableDice) {
        this.availableDice = availableDice;
    }
}
