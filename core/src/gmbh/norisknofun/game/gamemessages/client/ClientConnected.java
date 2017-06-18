package gmbh.norisknofun.game.gamemessages.client;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * Message sent internally by to notify that a new client has been connected.
 *
 * <p>
 *     This message is used on the server and the client side.
 * </p>
 */
public class ClientConnected implements Message {

    @Override
    public Class<? extends Message> getType() {
        return getClass();
    }
}
