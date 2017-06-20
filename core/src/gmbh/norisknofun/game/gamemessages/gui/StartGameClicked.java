package gmbh.norisknofun.game.gamemessages.gui;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * Message class sent by the UI (lobby scene) when host pressed the start game button.
 */
public class StartGameClicked implements Message {

    public StartGameClicked() {
    }

    @Override

    public Class<? extends Message> getType() {
        return getClass();
    }
}
