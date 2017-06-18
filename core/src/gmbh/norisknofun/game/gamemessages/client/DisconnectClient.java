package gmbh.norisknofun.game.gamemessages.client;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * Special message which can be sent to disconnect a client.
 *
 * <p>
 *     This message is processed on the server to force client disconnect.
 * </p>
 */
public class DisconnectClient implements Message {

    /** boolean flag indicating whether to terminate the client or not. */
    private final boolean terminateClient;

    /**
     * Constructor.
     * @param terminateClient If this flag is set to true, then the client connection is terminated,
     *                        means no pending messages are sent, otherwise they will be finished.
     */
    public DisconnectClient(boolean terminateClient) {

        this.terminateClient = terminateClient;
    }

    public boolean isTerminateClient() {

        return terminateClient;
    }

    @Override
    public Class<? extends Message> getType() {
        return null;
    }
}
