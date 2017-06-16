package gmbh.norisknofun.game.protocol.messages.handshake;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * Message class sent by server to client, when client's handshake request is accepted.
 */
public class HandshakeAccepted extends HandshakeResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Initialize the message with the protocol version spoken by the server.
     * @param serverProtocolVersion Protocol version spoken by server.
     */
    public HandshakeAccepted(String serverProtocolVersion) {

        super(serverProtocolVersion);
    }
}
