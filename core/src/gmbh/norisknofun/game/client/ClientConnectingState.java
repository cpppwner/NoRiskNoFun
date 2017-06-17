package gmbh.norisknofun.game.client;

import gmbh.norisknofun.network.Session;

/**
 * Initial state, when a client is starting to connect.
 *
 * <p>
 *     Wait until a new session arrives.
 * </p>
 */
class ClientConnectingState extends ClientStateBase {

    ClientConnectingState(Client client) {
        super(client);
    }

    @Override
    public void handleNewSession(Session newSession) {

        setSession(newSession);
        setNextState(new ClientHandshakeState(getClient()));
    }
}
