package gmbh.norisknofun.network;


import java.util.List;

import org.omg.PortableServer.SERVANT_RETENTION_POLICY_ID;

import gmbh.norisknofun.game.Player;
import gmbh.norisknofun.game.networkmessages.BasicMessageImpl;
import gmbh.norisknofun.game.networkmessages.waitingforplayers.PlayerJoined;
import gmbh.norisknofun.game.statemachine.server.ServerContext;

/**
 * Created by Philipp Mödritscher on 22.05.2017.
 */

public class ServerSessionEventHandlerImpl implements SessionEventHandler {

    private ServerContext context;
    public ServerSessionEventHandlerImpl(ServerContext context){
        this.context=context;
    }
    @Override
    public synchronized void newSession(Session session) {
        context.getGameData().addPlayer(new Player(session));
    }

    @Override
    public synchronized void sessionClosed(Session session) {
        List<Player> players=context.getGameData().getPlayers();
        for(int i=0; i<players.size(); i++){
            if(players.get(i).getSession().equals(session)){
                players.remove(i);
            }
        }
    }

    @Override
    public void sessionDataReceived(Session session) {
       //todo get message
        //set session in PlayerJoined Message Object
      // context.getState().handleMessage();
    }

    @Override
    public void sessionDataWritten(Session session) {
        //TODO
    }
}
