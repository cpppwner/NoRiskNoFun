package gmbh.norisknofun.game.protocol.messages.handshake;

import java.io.Serializable;

/**
 * Message class sent by server to client, when client's handshake request was rejected.
 */
public class HandshakeRejected extends HandshakeResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Create handshake rejected message.
     * @param serverProtocolVersion Protocol version spoken by server.
     */
    public HandshakeRejected(String serverProtocolVersion) {

        super(serverProtocolVersion);
    }
}
