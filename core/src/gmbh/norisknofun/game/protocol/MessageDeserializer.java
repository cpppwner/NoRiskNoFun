package gmbh.norisknofun.game.protocol;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.protocol.util.MessageBuffer;

/**
 * Class used to deserialize a byte[] into a message.
 *
 * <p>
 *     This class is the inverse of {@link MessageSerializer}.
 * </p>
 */
public final class MessageDeserializer {

    /**
     * {@link MessageBuffer} storing the byte data, that was received.
     *
     * <p>
     *     This is required, since it cannot be guaranteed that a {@link Message}
     *     is received as whole.
     * </p>
     */
    private final MessageBuffer buffer;

    /**
     * Constructor taking message buffer.
     *
     * @param buffer The message buffer from where to read bytes.
     */
    public MessageDeserializer(MessageBuffer buffer) {

        if (buffer == null) {
            throw new IllegalArgumentException("buffer is null");
        }

        this.buffer = buffer;
    }

    /**
     * Checks if there is a message to deserialize.
     *
     * <p>
     *     This does just some rough checks and not full deserialization checks.
     * </p>
     *
     * @return {@code true} If enough bytes are available in {@link MessageBuffer}
     * to deserialize a {@link Message}, {@code false} otherwise.
     *
     * @throws ProtocolException
     */
    public boolean hasMessageToDeserialize() throws ProtocolException {

        if (buffer.length() < ProtocolConstants.MESSAGE_LENGTH_BYTES + ProtocolConstants.TYPE_IDENTIFIER_LENGTH_BYTES) {
            // minimum 3 bytes
            return false;
        }

        return buffer.length() >= decodeMessageLength();
    }

    /**
     * Deserialize a Message class from byte[].
     *
     * <p>
     *     The byte[] data is taken from the {@link MessageBuffer} passed in the constructor.
     * </p>
     *
     * @return Deserialized message.
     * @throws ProtocolException In case of protocol error, meaning somethings wrong with the byte data.
     * @throws IOException If an I/O error occurs.
     */
    public Message deserialize() throws ProtocolException, IOException {

        if (!hasMessageToDeserialize()) {
            throw new IllegalStateException("No message to deserialize");
        }

        int messageLength = decodeMessageLength();
        byte[] payload = buffer.read(messageLength);

        // ensure the message type is valid.
        Class cls = deserializeType(payload);

        // get payload of message and deserialize it
        byte[] messagePayload = extractMessagePayload(payload);
        Object object = deserializeMessage(messagePayload);

        if (!object.getClass().getCanonicalName().equals(cls.getCanonicalName())) {
            throw new ProtocolException("specified message class and deserialized object are not equal");
        }

        return (Message)object;
    }

    /**
     * Decode the message length.
     *
     * <p>
     *     The message length is specified as the first two bytes in big-endian notation
     *     as unsigned 16-bit value.
     * </p>
     *
     * @return Decoded message length in bytes.
     */
    private int decodeMessageLength() {

        byte[] data = buffer.peek(ProtocolConstants.MESSAGE_LENGTH_BYTES);
        return ((data[0] & 0xFF) << 8) | (data[1] & 0xFF);
    }

    /**
     * Deserialize the Message type from the raw message.
     *
     * @param messageData The raw message as byte[].
     * @return Deserialized message type.
     * @throws ProtocolException If no class can be loaded for type name
     * or loaded class does not implement {@link Message} interface.
     */
    private static Class deserializeType(byte[] messageData) throws ProtocolException {

        int messageTypeLength = messageData[ProtocolConstants.MESSAGE_LENGTH_BYTES] & 0xFF;

        byte[] messageTypeData = new byte[messageTypeLength];
        System.arraycopy(messageData,
                         ProtocolConstants.MESSAGE_LENGTH_BYTES + ProtocolConstants.TYPE_IDENTIFIER_LENGTH_BYTES,
                         messageTypeData,
                         0,
                         messageTypeData.length);

        Class messageClass;
        String messageClassName = new String(messageTypeData, ProtocolConstants.TYPE_SERIALIZATION_CHARSET);
        try {
            messageClass = Class.forName(messageClassName);
        } catch (ClassNotFoundException e) {
            throw new ProtocolException("Class \"" + messageClassName + "\" is unknown");
        }

        if (!Message.class.isAssignableFrom(messageClass)) {
            throw new ProtocolException("Class \"" + messageClassName + "\" is not implementing Message interface");
        }

        return messageClass;
    }

    /**
     * Extract the message payload from raw data.
     *
     * <p>
     *     The message payload is the serialized message data only,
     *     with all protocol overhead already removed.
     * </p>
     *
     * @param messageData The raw message data.
     * @return Extracted message payload.
     */
    private static byte[] extractMessagePayload(byte[] messageData) {

        int messageTypeLength = messageData[ProtocolConstants.MESSAGE_LENGTH_BYTES] & 0xFF;
        int payloadOffset = ProtocolConstants.MESSAGE_LENGTH_BYTES + ProtocolConstants.TYPE_IDENTIFIER_LENGTH_BYTES + messageTypeLength;

        byte[] payload = new byte[messageData.length - payloadOffset];
        System.arraycopy(messageData, payloadOffset, payload, 0, payload.length);

        return payload;
    }

    /**
     * Deserialize the message class.
     *
     * @param messagePayload The payload containing serialized class data.
     * @return Deserialized object.
     * @throws ProtocolException If payload is empty or deserialization fails.
     * @throws IOException If an I/O error occurs.
     */
    private static Object deserializeMessage(byte[] messagePayload) throws ProtocolException, IOException {

        if (messagePayload.length == 0) {
            throw new ProtocolException("message payload is empty");
        }

        Object result;
        try (ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(messagePayload)) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(arrayInputStream)) {
                result = objectInputStream.readObject();
            } catch (Exception e) {
                throw new ProtocolException("decoding message payload yields invalid class");
            }
        }

        return result;
    }
}
