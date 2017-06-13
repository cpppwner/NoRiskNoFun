package gmbh.norisknofun.game.statemachine.server;

import com.badlogic.gdx.Gdx;

import java.awt.Color;

import gmbh.norisknofun.game.ColorPool;
import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerJoined;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerAccepted;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerRejected;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayersInGame;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.StartGame;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by pippp on 15.05.2017.
 */

public class WaitingForPlayersState extends State {

    private ServerContext context;
    private final GameDataServer data;
    ColorPool colorPool;
    public WaitingForPlayersState(ServerContext context){

        this.context=context;
        this.data=context.getGameData();
        colorPool= new ColorPool();
    }


    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void handleMessage(String senderId, Message message) {

        if(message.getType().equals(PlayerJoined.class)){
            addPlayer((PlayerJoined) message, senderId);
        }else if(message.getType().equals(StartGame.class) ){
            startGame();
        }else{
            Gdx.app.log("Server: WaitingforPlayerState","message unknown: " + message.getType().getSimpleName());
        }
    }

    private void  startGame(){
        context.sendMessage(new StartGame(true));
        context.setState(new SpreadTroopsState(context));
    }


    private void addPlayer(PlayerJoined message, String senderId){
        PlayerAccepted playerAccepted = new PlayerAccepted(message.getPlayerName());
        Color color = colorPool.getNextAvailableColor();
        int playerColor = color.getRGB();
        playerAccepted.setPlayerColor(playerColor);
        playerAccepted.setMaxNumPlayers(context.getGameData().getMaxPlayer());
        try{
            playerAccepted.setMapName(context.getGameData().getMapAsset().getName());
        }catch(IllegalStateException e){
            //Map not set

        }

        playerAccepted.setPlayerId(senderId);

        PlayersInGame playersInGame = new PlayersInGame();

        playersInGame.setAllPlayers(context.getGameData().getPlayers());

        if(data.getPlayers().addPlayer(new Player(message.getPlayerName(),senderId,playerColor))) {



            context.sendMessage(playersInGame);
            context.sendMessage(playerAccepted,senderId);

        }
        else {
            PlayerRejected playerRejected = new PlayerRejected();
            colorPool.relaseColor(color);
            context.sendMessage(playerRejected,senderId);
        }

    }




}
