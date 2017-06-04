package gmbh.norisknofun.game.statemachine.server;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerJoined;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerJoinedCheck;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.StartGame;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by pippp on 15.05.2017.
 */

public class WaitingForPlayersState extends State {

    private ServerContext context;
    private final GameDataServer data;
    public WaitingForPlayersState(ServerContext context){

        this.context=context;
        this.data=context.getGameData();
    }


    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void handleMessage(String senderId, BasicMessageImpl message) {

        if(message.getType().equals(PlayerJoined.class)){
            addPlayer((PlayerJoined) message, senderId);
        }else if(message.getType().equals(StartGame.class) ){
            startGame((StartGame)message);
        }else{
            Gdx.app.log("WaitingforPlayerState","message unknown");
        }
    }

    private void  startGame(StartGame message){
        //todo notify clients to change state
        context.setState(new SpreadTroopsState(context));
    }


    private void addPlayer(PlayerJoined message, String senderId){
        boolean check=false;
        PlayerJoinedCheck playerJoinedCheck = new PlayerJoinedCheck(message.getPlayerName(),senderId);
        if(data.getPlayers().addPlayer(new Player(message.getPlayerName(),senderId))) {
            playerJoinedCheck.setAllowedtojoin(true);
        }
        else {
            playerJoinedCheck.setAllowedtojoin(false);
        }
        context.sendMessage(playerJoinedCheck,senderId);
    }




}
