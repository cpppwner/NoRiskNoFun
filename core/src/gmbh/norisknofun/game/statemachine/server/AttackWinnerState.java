package gmbh.norisknofun.game.statemachine.server;

import gmbh.norisknofun.game.GameDataServer;
import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by pippp on 15.05.2017.
 */

public class AttackWinnerState extends State {

    private ServerContext context;
    private AttackState state;
    private final GameDataServer data;
    public AttackWinnerState(ServerContext context, AttackState state){
        this.context=context;
        this.data=context.getGameData();
        this.state=state;
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }

    @Override
    public void handleMessage(String senderId, Message message) {

    }
}
