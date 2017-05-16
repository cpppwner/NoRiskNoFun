package gmbh.norisknofun.game.networkmessages;

import java.io.Serializable;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

public class BasicMessageImpl implements Message,Serializable {


    @Override
    public Class<? extends Message> getType() {
        return null;
    }
}
