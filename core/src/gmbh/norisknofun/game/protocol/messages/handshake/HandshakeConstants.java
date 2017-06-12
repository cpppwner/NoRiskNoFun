package gmbh.norisknofun.game.protocol.messages.handshake;

/**
 * Class containing some constants related to our hand shake process.
 *
 * <p>
 *     Since this class must not be instantiated the constructor is private.
 * </p>
 */
public final class HandshakeConstants {

    /**
     * The magic string expected in the handshake message.
     *
     * <p>
     *     This magic string must stay the same for different protocol versions.
     * </p>
     */
    public static final String HANDSHAKE_MAGIC = "NoRiskNoFun";

    /**
     * The protocol version "spoken" by the server.
     */
    public static final String HANDSHAKE_PROTOCOL_VERSION = "1.0.0";

    /**
     * Not allowed to create an instance.
     */
    private HandshakeConstants() {}
}
