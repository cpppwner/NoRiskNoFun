package gmbh.norisknofun.game.client;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.network.Session;

/**
 * Created by cpppwner on 05.06.17.
 */

interface ClientState {

    void enter();

    void exit();

    void handleOutboundMessage(Message message);

    void handleNewSession(Session newSession);

    void handleSessionClosed(Session closedSession);

    void handleDataReceived();
}
