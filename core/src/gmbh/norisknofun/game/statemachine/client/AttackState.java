package gmbh.norisknofun.game.statemachine.client;

import gmbh.norisknofun.game.statemachine.DelegatingState;

/**
 * Created by Katharina on 19.05.2017.
 */

class AttackState extends DelegatingState {

    AttackState(ClientContext context, boolean isAttacker){
        if (isAttacker) {
            initializeState(new ChooseTroopAmountState(context, this));
        } else {
            initializeState(new EvaluateDiceResultState(context));
        }
    }
}
