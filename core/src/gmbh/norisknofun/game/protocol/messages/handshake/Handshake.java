package gmbh.norisknofun.game.protocol.messages.handshake;

import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * Message sent by client, when performing initial handshake operation.
 */
public class Handshake implements Message, Serializable {

    private static final long serialVersionUID = 1L;

    /** magic string */
    private String protocolMagic;
    /** version spoken by client */
    private String protocolVersion;

    /**
     * Initialize handshake message with given magic string and protocol version.
     * @param protocolMagic The magic string.
     * @param protocolVersion Protocol version spoken by client.
     */
    public Handshake(String protocolMagic, String protocolVersion) {
        setProtocolMagic(protocolMagic);
        setProtocolVersion(protocolVersion);
    }

    @Override
    public Class<? extends Message> getType() {

        return getClass();
    }

    /**
     * Get the protocol magic passed in constructor.
     */
    public String getProtocolMagic() {

        return protocolMagic;
    }

    /**
     * Set the protocol magic and perform some checks.
     */
    private void setProtocolMagic(String protocolMagic) {

        if (protocolMagic == null || protocolMagic.isEmpty())
            throw new IllegalArgumentException("protocolMagic is null or empty");

        this.protocolMagic = protocolMagic;
    }

    /**
     * Get the protocol version.
     */
    public String getProtocolVersion() {

        return protocolVersion;
    }

    /**
     * Set the protocol version and perform some checks.
     */
    private void setProtocolVersion(String protocolVersion) {

        if (protocolVersion == null || protocolVersion.isEmpty())
            throw new IllegalArgumentException("protocolVersion is null or empty");

        this.protocolVersion = protocolVersion;
    }
}
