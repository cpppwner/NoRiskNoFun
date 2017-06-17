package gmbh.norisknofun.game.gamemessages.client;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * Special class to notify the server that a client was disconnected.
 *
 * <p>
 *     This message is sent on server and client side.
 * </p>
 */
public class ClientDisconnected implements Message {

    @Override
    public Class<? extends Message> getType() {
        return getClass();
    }
}
