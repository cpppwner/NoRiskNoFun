package gmbh.norisknofun.game.protocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import gmbh.norisknofun.game.networkmessages.Message;

/**
 * Serializer class used to serialize a {@link Message} into a byte[].
 */
public final class MessageSerializer {

    private final Message message;

    /**
     * Constructor taking the message to serialize.
     *
     * @param message Message to serialize into a byte array.
     */
    public MessageSerializer(Message message) {

        if (message == null)
            throw new IllegalArgumentException("message is null");

        this.message = message;
    }

    /**
     * Serialize the {@link Message} given in the constructor into a byte array.
     *
     * @return The serialized message as byte array.
     * @throws IOException If an I/O error occurs.
     * @throws ProtocolException If given message cannot be serialized.
     */
    public byte[] serialize() throws IOException, ProtocolException {

        byte[] type = serializeType();
        byte[] content = serializeMessageContent();

        int totalLength = type.length + content.length;
        if (totalLength > ProtocolConstants.MAX_MESSAGE_LENGTH) {
            throw new ProtocolException("Message is too long");
        }

        byte[] result = new byte[totalLength + ProtocolConstants.MESSAGE_LENGTH_BYTES];
        result[0] = (byte)(result.length >> 8);
        result[1] = (byte)(result.length & 0xFF);
        System.arraycopy(type, 0, result, ProtocolConstants.MESSAGE_LENGTH_BYTES, type.length);
        System.arraycopy(content, 0, result, ProtocolConstants.MESSAGE_LENGTH_BYTES + type.length, content.length);

        return result;
    }

    /**
     * Serialize the message type.
     * @return Serialized message type.
     * @throws ProtocolException If given message cannot be serialized.
     */
    private byte[] serializeType() throws ProtocolException {

        String typeName = message.getType().getCanonicalName();
        if (typeName == null) {
            throw new ProtocolException("Message type does not have canonical name");
        }

        byte[] typeIdentifier = typeName.getBytes(ProtocolConstants.TYPE_SERIALIZATION_CHARSET);
        if (typeIdentifier.length > ProtocolConstants.MAX_TYPE_IDENTIFIER_LENGTH) {
            throw new IllegalStateException("Message type identifier is too long");
        }

        byte[] serializedType = new byte[typeIdentifier.length + ProtocolConstants.TYPE_IDENTIFIER_LENGTH_BYTES];
        serializedType[0] = (byte)(typeIdentifier.length & 0xFF); // length of type
        System.arraycopy(typeIdentifier, 0, serializedType, ProtocolConstants.TYPE_IDENTIFIER_LENGTH_BYTES, typeIdentifier.length);

        return serializedType;
    }

    /**
     * Serialize message content.
     * @return Serialized message content.
     * @throws IOException In case of an I/O error.
     */
    private byte[] serializeMessageContent() throws IOException {

        byte[] result;

        try (ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream()) {
            try (ObjectOutputStream objectStream = new ObjectOutputStream(arrayOutputStream)) {
                objectStream.writeObject(message);
                result = arrayOutputStream.toByteArray();
            }
        }

        return result;
    }
}
