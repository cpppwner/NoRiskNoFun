package gmbh.norisknofun.game.protocol;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.protocol.util.MessageBuffer;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for {@link MessageDeserializer}.
 */
public class MessageDeserializerTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void constructingMessageDeserializerWithNullArgumentThrowsException() {

        // given
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("buffer is null");

        // when constructing with null argument, then exception is thrown
        new MessageDeserializer(null);
    }

    @Test
    public void whenMessageLengthIsNotInBufferThereIsNoMessageToDeserialize()  {

        // given
        MessageBuffer buffer  = new MessageBuffer();
        MessageDeserializer target = new MessageDeserializer(buffer);

        // when buffer is empty
        boolean obtained = target.hasMessageToDeserialize();

        // then
        assertThat(obtained, is(false));

        // and when adding only one byte
        buffer.append(new byte[] { (byte)0x00 });
        obtained = target.hasMessageToDeserialize();

        // then
        assertThat(obtained, is(false));
    }

    @Test
    public void hasMessageToDeserializeReturnsFalseWhenToFewBytesAreInBuffer() {

        // given
        MessageBuffer buffer  = new MessageBuffer();
        buffer.append(new byte[] {(byte)0x00, (byte)0x05, (byte)0x00, (byte)0x00});
        MessageDeserializer target = new MessageDeserializer(buffer);

        // when buffer contains less bytes than length field specifies
        boolean obtained = target.hasMessageToDeserialize();

        // then
        assertThat(obtained, is(false));
    }

    @Test
    public void hasMessageToDeserializeReturnsTrueWhenBufferContainsEnoughBytes() {

        // given
        MessageBuffer buffer = new MessageBuffer();
        buffer.append(new byte[]{(byte) 0x00, (byte) 0x05, (byte) 0x00, (byte) 0x00, (byte) 0x00});
        MessageDeserializer target = new MessageDeserializer(buffer);

        // when buffer contains exactly the amount of bytes specified by message length
        boolean obtained = target.hasMessageToDeserialize();

        // then
        assertThat(obtained, is(true));

        // and when adding some more bytes to buffer
        buffer.append(new byte[]{0x00});
        obtained = target.hasMessageToDeserialize();

        // then
        assertThat(obtained, is(true));
    }

    @Test
    public void deserializeThrowsExceptionWhenThereIsNoMessageToDeserialize() throws IOException, ProtocolException {

        // given
        MessageDeserializer target = new MessageDeserializer(new MessageBuffer());

        expectedException.expect(IllegalStateException.class);
        expectedException.expectMessage(is("No message to deserialize"));

        // when there is no message to deserialize, then
        target.deserialize();
    }

    @Test
    public void deserializeThrowsExceptionWhenMessageIdentifierIsEmpty() throws IOException, ProtocolException {

        // given
        MessageBuffer buffer = new MessageBuffer();
        buffer.append(new byte[] {(byte)0x00, (byte)0x03, (byte)0x00});
        MessageDeserializer target = new MessageDeserializer(buffer);

        expectedException.expect(ProtocolException.class);
        expectedException.expectMessage(is("Class \"\" is unknown"));

        // when there is no type identifier, then
        target.deserialize();
    }

    @Test
    public void deserializeThrowsExceptionWhenMessageIdentifierIsUnknownClass() throws IOException, ProtocolException {

        // given
        byte[] messageType = "foo.bar.FooBar".getBytes(ProtocolConstants.TYPE_SERIALIZATION_CHARSET);
        int totalLength = messageType.length + ProtocolConstants.MESSAGE_LENGTH_BYTES + ProtocolConstants.TYPE_IDENTIFIER_LENGTH_BYTES;

        MessageBuffer buffer = new MessageBuffer();
        buffer.append(new byte[] {(byte)((totalLength >> 8) & 0xFF), (byte)(totalLength & 0xFF),
                                  (byte)(messageType.length & 0xFF)});
        buffer.append(messageType);

        MessageDeserializer target = new MessageDeserializer(buffer);

        expectedException.expect(ProtocolException.class);
        expectedException.expectMessage(is("Class \"foo.bar.FooBar\" is unknown"));

        // when there is no type identifier, then
        target.deserialize();
    }

    @Test
    public void deserializeThrowsExceptionWhenMessageIdentifierIsValidClassButDoesNotImplementMessageInterface() throws IOException, ProtocolException {

        // given
        byte[] messageType = NotAMessageClass.class.getName().getBytes(ProtocolConstants.TYPE_SERIALIZATION_CHARSET);
        int totalLength = messageType.length + ProtocolConstants.MESSAGE_LENGTH_BYTES + ProtocolConstants.TYPE_IDENTIFIER_LENGTH_BYTES;

        MessageBuffer buffer = new MessageBuffer();
        buffer.append(new byte[] {(byte)((totalLength >> 8) & 0xFF), (byte)(totalLength & 0xFF),
                (byte)(messageType.length & 0xFF)});
        buffer.append(messageType);

        MessageDeserializer target = new MessageDeserializer(buffer);

        expectedException.expect(ProtocolException.class);
        expectedException.expectMessage(is("Class \"" + NotAMessageClass.class.getName() + "\" is not implementing Message interface"));

        // when there is no type identifier, then
        target.deserialize();
    }

    @Test
    public void deserializeThrowsExceptionWhenMessagePayloadIsEmpty() throws IOException, ProtocolException {

        // given
        byte[] messageType = MessageClass.class.getName().getBytes(ProtocolConstants.TYPE_SERIALIZATION_CHARSET);
        int totalLength = messageType.length + ProtocolConstants.MESSAGE_LENGTH_BYTES + ProtocolConstants.TYPE_IDENTIFIER_LENGTH_BYTES;

        MessageBuffer buffer = new MessageBuffer();
        buffer.append(new byte[] {(byte)((totalLength >> 8) & 0xFF), (byte)(totalLength & 0xFF),
                (byte)(messageType.length & 0xFF)});
        buffer.append(messageType);

        MessageDeserializer target = new MessageDeserializer(buffer);

        expectedException.expect(ProtocolException.class);
        expectedException.expectMessage(is("message payload is empty"));

        // when there is no type identifier, then
        target.deserialize();
    }

    @Test
    public void deserializeThrowsExceptionWhenMessagePayloadCannotBeDeserialized() throws IOException, ProtocolException {

        // given
        byte[] messageType = MessageClass.class.getName().getBytes(ProtocolConstants.TYPE_SERIALIZATION_CHARSET);
        byte[] messagePayload = serializeObject(new AnotherMessageClass());
        for (int i = 0; i < messagePayload.length; i++) {
            messagePayload[i] = (byte)0xFF;
        }
        int totalLength = messageType.length + messagePayload.length +  ProtocolConstants.MESSAGE_LENGTH_BYTES + ProtocolConstants.TYPE_IDENTIFIER_LENGTH_BYTES;

        MessageBuffer buffer = new MessageBuffer();
        buffer.append(new byte[] {(byte)((totalLength >> 8) & 0xFF), (byte)(totalLength & 0xFF),
                (byte)(messageType.length & 0xFF)});
        buffer.append(messageType);
        buffer.append(messagePayload);

        MessageDeserializer target = new MessageDeserializer(buffer);

        expectedException.expect(ProtocolException.class);
        expectedException.expectMessage(is("decoding message payload yields invalid class"));

        // when there is no type identifier, then
        target.deserialize();
    }

    @Test
    public void deserializeThrowsExceptionWhenMessageTypeAndPayloadDiffer() throws IOException, ProtocolException {

        // given
        byte[] messageType = MessageClass.class.getName().getBytes(ProtocolConstants.TYPE_SERIALIZATION_CHARSET);
        byte[] messagePayload = serializeObject(new AnotherMessageClass());
        int totalLength = messageType.length + messagePayload.length +  ProtocolConstants.MESSAGE_LENGTH_BYTES + ProtocolConstants.TYPE_IDENTIFIER_LENGTH_BYTES;

        MessageBuffer buffer = new MessageBuffer();
        buffer.append(new byte[] {(byte)((totalLength >> 8) & 0xFF), (byte)(totalLength & 0xFF),
                (byte)(messageType.length & 0xFF)});
        buffer.append(messageType);
        buffer.append(messagePayload);

        MessageDeserializer target = new MessageDeserializer(buffer);

        expectedException.expect(ProtocolException.class);
        expectedException.expectMessage(is("specified message class and deserialized object are not equal"));

        // when there is no type identifier, then
        target.deserialize();
    }

    @Test
    public void deserializeMessageGivesCorrectMessage() throws IOException, ProtocolException {

        // given
        MessageClass originalMessage = new MessageClass();
        originalMessage.setNumber(42);

        MessageBuffer buffer = new MessageBuffer();
        buffer.append(new MessageSerializer(originalMessage).serialize());

        MessageDeserializer target = new MessageDeserializer(buffer);

        // when
        Message obtained = target.deserialize();

        // then
        assertThat(obtained, is(notNullValue()));
        assertThat(obtained, is(instanceOf(MessageClass.class)));
        assertThat((MessageClass)obtained, is(not(sameInstance(originalMessage))));
        assertThat(((MessageClass)obtained).getNumber(), is(originalMessage.getNumber()));
    }

    /**
     * Helper method to serialize object.
     *
     * @param o The object to serialize.
     * @return Serialized byte array.
     */
    private static byte[] serializeObject(Object o) throws IOException {

        byte[] result;

        try (ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream()) {
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(arrayOutputStream)) {
                objectOutputStream.writeObject(o);
            }
            result = arrayOutputStream.toByteArray();
        }

        return result;
    }

    private static class NotAMessageClass implements Serializable {

        private static final long serialVersionUID = 1L;
    }

    private static class MessageClass implements Message, Serializable {

        private static final long serialVersionUID = 1L;

        private int number;

        @Override
        public Class<? extends Message> getType() {
            return getClass();
        }

        int getNumber() { return number; }
        void setNumber(int number) { this.number = number; }
    }

    private static class AnotherMessageClass implements Message, Serializable {

        private static final long serialVersionUID = 1L;

        @Override
        public Class<? extends Message> getType() {
            return getClass();
        }
    }
}
