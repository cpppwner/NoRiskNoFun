package gmbh.norisknofun.game.statemachine.server;

import gmbh.norisknofun.game.statemachine.DelegatingState;

/**
 * Created by pippp on 15.05.2017.
 */

class AttackState extends DelegatingState {

    AttackState(ServerContext context){
        super();
        initializeState(new ChooseTroopAmountState(context, this));

    }

}
