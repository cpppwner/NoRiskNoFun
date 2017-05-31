package gmbh.norisknofun.game.statemachine.server;

import com.badlogic.gdx.Gdx;

import gmbh.norisknofun.game.GameData;
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
    private final GameData data;
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
    public void handleMessage(BasicMessageImpl message) {

        if(message.getType().equals(PlayerJoined.class)){
            addPlayer((PlayerJoined) message);
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


    private void addPlayer(PlayerJoined message){
        boolean check=false;
        PlayerJoinedCheck playerJoinedCheck = new PlayerJoinedCheck(message.playername);
        if(!checkIfPlayernameAlreadyExists(message.playername))
         {
             // TODO - nope - the session is definitely not exposed public
            Player player = null; //getPlayerWithSession(message.session);
            player.setPlayername(message.playername);
            playerJoinedCheck.allowedtojoin=true;

        }else {
            playerJoinedCheck.allowedtojoin=false;
        }
        context.sendMessage(playerJoinedCheck);
    }

    private boolean checkIfPlayernameAlreadyExists(String playername){
        boolean check=false;
        for(Player p: data.getPlayers()){
            if(p.getPlayername().equals(playername)){
                check=true;
            }
        }
        return check;
    }


    /*
    ** TODO - whoever did this, nope the session is not exposed to the state
    * and definitely not the implemenation, that's package internal stuff only
    private Player getPlayerWithSession(SessionImpl session){
        Player result=null;
        for(Player player: context.getGameData().getPlayers()){
            if(player.getSession().equals(session)){
                result=player;
            }
        }
        return result;
    }
    */
}
