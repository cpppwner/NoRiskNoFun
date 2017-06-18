package gmbh.norisknofun.game.protocol;

import java.nio.charset.Charset;

/**
 * Class storing some constants for the protocol.
 */
public class ProtocolConstants {

    /**
     * The number of bytes specifying the total message length.
     *
     * <p>
     *     A message can be max. 2^16-1 bytes long in total, including the message length itself.
     * </p>
     */
    public static final int MESSAGE_LENGTH_BYTES = 2;

    /**
     * Maximum message length which is supported, excluding the two bytes length identifier.
     */
    public static final int MAX_MESSAGE_LENGTH = 65535 - MESSAGE_LENGTH_BYTES;

    /**
     * The number of bytes specifying message type length.
     */
    public static final int TYPE_IDENTIFIER_LENGTH_BYTES = 1;

    /**
     * Maximum length for the message type name in bytes.
     */
    public static final int MAX_TYPE_IDENTIFIER_LENGTH = 255;

    /**
     * Since types (classes) should be in ASCII range, UTF-8 serialization is used.
     */
    public static final Charset TYPE_SERIALIZATION_CHARSET = Charset.forName("UTF-8");

    /**
     * Default constructor.
     *
     * <p>
     *     This constructor is intentionally private,
     *     because no instance of this class should be created.
     * </p>
     */
    private ProtocolConstants() { }
}
