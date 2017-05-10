package gmbh.norisknofun.Network.NetworkMessages;

import java.io.Serializable;

/**
 * Created by Philipp Mödritscher on 10.05.2017.
 */

public class PlayerJoined implements Message,Serializable {

    String playername;

    @Override
    public Class<? extends Message> getType() {
        return null;
    }
}
