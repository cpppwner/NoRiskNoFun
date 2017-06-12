package gmbh.norisknofun.game.protocol.messages.handshake;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * Message class sent by server to client, when client's handshake request is accepted.
 */
public class HandshakeAccepted implements Message, Serializable {

    private static final long serialVersionUID = 1L;

    /** The protocol version spoken by the server. */
    private String serverProtocolVersion;

    /**
     * Initialize the message with the protocol version spoken by the server.
     * @param serverProtocolVersion Protocol version spoken by server.
     */
    public HandshakeAccepted(String serverProtocolVersion) {

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

    private void setServerProtocolVersion(String serverProtocolVersion) {

        if (serverProtocolVersion == null || serverProtocolVersion.isEmpty())
            throw new IllegalArgumentException("serverProtocolVersion is null or empty");

        this.serverProtocolVersion = serverProtocolVersion;
    }
}
