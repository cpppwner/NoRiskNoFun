package gmbh.norisknofun.game.statemachine.server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import gmbh.norisknofun.game.ColorPool;
import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.gamemessages.client.ClientConnected;
import gmbh.norisknofun.game.gamemessages.client.ClientDisconnected;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerAccepted;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerJoined;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerRejected;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayersInGame;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.StartGame;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Server side state that is used, when waiting for further players to join the game.
 */
class WaitingForPlayersState extends State {

    private final ServerContext context;
    private final GameDataServer data;
    private final ColorPool colorPool;

    WaitingForPlayersState(ServerContext context){

        this.context=context;
        this.data=context.getGameData();
        colorPool= new ColorPool();
    }

    @Override
    public void handleMessage(String senderId, Message message) {

        if(message.getType().equals(PlayerJoined.class)){
            addPlayer((PlayerJoined) message, senderId);
        }else if(message.getType().equals(StartGame.class)) {
            startGame();
        } else if (message.getType().equals(ClientConnected.class)) {
            // new client connected, don't care
        } else if (message.getType().equals(ClientDisconnected.class)) {
            // client disconnected, remote the
            removePlayer(senderId);
        } else {
            Gdx.app.log(getClass().getSimpleName(), "message unknown: " + message.getType().getName());
        }
    }

    private void  startGame(){
        context.sendMessage(new StartGame(true));
        context.setState(new SpreadTroopsState(context));
    }


    private void addPlayer(PlayerJoined message, String senderId) {

        // check if the player can be accepted
        if (data.isServerFull()) {
            context.sendMessage(new PlayerRejected("Server is full."), senderId);
        } else if (message.getPlayerName() == null
                || message.getPlayerName().isEmpty()
                || data.getPlayerByName(message.getPlayerName()) != null) {
            context.sendMessage(new PlayerRejected("Name is already in use."), senderId);
        } else {
            // all fine, accept player
            acceptNewPlayer(message, senderId);
            context.sendMessage(new PlayersInGame(data.getPlayers()));
        }
    }


    private void removePlayer(String senderId) {

        Player player = data.getPlayers().getPlayerByID(senderId);
        if (player != null) {
            data.getPlayers().removePlayer(player.getPlayerName());
            colorPool.releaseColor(new Color(player.getColor()));
            context.sendMessage(new PlayersInGame(data.getPlayers())); // update all connected clients
        }
    }

    private void acceptNewPlayer(PlayerJoined message, String senderId) {

        Player newPlayer = new Player(message.getPlayerName(), senderId, Color.rgba8888(colorPool.getNextAvailableColor()));
        data.getPlayers().addPlayer(newPlayer);

        PlayerAccepted playerAccepted = new PlayerAccepted(newPlayer);
        playerAccepted.setMapName(data.getMapFilename());
        playerAccepted.setMaxNumPlayers(data.getMaxPlayer());
        context.sendMessage(playerAccepted, senderId);
    }
}
