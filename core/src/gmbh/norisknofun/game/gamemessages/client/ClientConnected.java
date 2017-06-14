package gmbh.norisknofun.game.gamemessages.client;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * Created by cpppwner on 12.06.17.
 */

public class ClientConnected implements Message {

    @Override
    public Class<? extends Message> getType() {
        return getClass();
    }
}
