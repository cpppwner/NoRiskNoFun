package gmbh.norisknofun.game.gamemessages.client;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * Created by cpppwner on 12.06.17.
 */

public class ClientConnectionRefused implements Message {

    private final String reason;

    public ClientConnectionRefused(String reason) {

        this.reason = reason;
    }

    @Override
    public Class<? extends Message> getType() {
        return getClass();
    }

    public String getReason() {

        return reason;
    }
}
