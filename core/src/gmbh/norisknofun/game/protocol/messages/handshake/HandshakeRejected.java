package gmbh.norisknofun.game.protocol.messages.handshake;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * Message class sent by server to client, when client's handshake request was rejected.
 */
public class HandshakeRejected implements Message, Serializable {

    private static final long serialVersionUID = 1L;

    /** protocol version spoken by server */
    private String serverProtocolVersion;

    /**
     * Create handshake rejected message.
     * @param serverProtocolVersion Protocol version spoken by server.
     */
    public HandshakeRejected(String serverProtocolVersion) {

        setServerProtocolVersion(serverProtocolVersion);
    }

    @Override
    public Class<? extends Message> getType() {

        return getClass();
    }

    /**
     * Get server's protocol version.
     */
    public String getServerProtocolVersion() {

        return serverProtocolVersion;
    }

    /**
     * Initialize server's protocol version and perform some checks.
     */
    private void setServerProtocolVersion(String serverProtocolVersion) {

        if (serverProtocolVersion == null || serverProtocolVersion.isEmpty())
            throw new IllegalArgumentException("serverProtocolVersion is null or empty");

        this.serverProtocolVersion = serverProtocolVersion;
    }
}
