package gmbh.norisknofun.game.protocol.messages.handshake;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * Base message for all handshake results (accepted/rejected).
 */
abstract class HandshakeResult implements Message, Serializable {

    private static final long serialVersionUID = 1L;

    /** protocol version spoken by server */
    private final String serverProtocolVersion;

    HandshakeResult(String serverProtocolVersion) {

        if (serverProtocolVersion == null || serverProtocolVersion.isEmpty())
            throw new IllegalArgumentException("serverProtocolVersion is null or empty");

        this.serverProtocolVersion = serverProtocolVersion;
    }

    @Override
    public Class<? extends Message> getType() {

        return getClass();
    }

    /**
     * Get server's protocol version.
     *
     * @return protocol version
     */
    public String getServerProtocolVersion() {

        return serverProtocolVersion;
    }
}
