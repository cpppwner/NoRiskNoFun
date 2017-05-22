package gmbh.norisknofun.game.networkmessages;

import gmbh.norisknofun.game.statemachine.State;

/**
 * Created by pippp on 15.05.2017.
 */

public class ChangeState extends BasicMessageImpl {
    public State state;

    public ChangeState(State state) {
        this.state = state;
    }
    // TODO: 15.05.2017 how to identify correct state on client
}
