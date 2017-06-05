package gmbh.norisknofun.game.protocol;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.charset.Charset;

import gmbh.norisknofun.game.networkmessages.Message;


import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link MessageSerializer}.
 */
public class MessageSerializerTests {

    private static String loremIpsum;
    private static String repeatedLoremIpsum;

    @BeforeClass
    public static void setUpFixture() {

        StringBuilder sb = new StringBuilder();
        sb.append("Lorem ipsum dolor sit amet, eam no tale solet patrioque, est ")
                .append("ne dico veri. Copiosae petentium no eum, has at wisi dicunt causae. Duo ea ")
                .append("animal eligendi honestatis, dico fastidii officiis sit ne. At oblique ")
                .append("docendi verterem ius, te vide cibo gloriatur nam. Ad has possit delicata. ")
                .append("Sit vocibus accusamus an.");
        loremIpsum = sb.toString();

        sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append(loremIpsum);
        }

        repeatedLoremIpsum = sb.toString();
    }

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void passingNullToMessageSerializerConstructorThrowsException() {

        // given
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("message is null"));

        // when instantiating with null, then expect exception
        new MessageSerializer(null);
    }

    @Test
    public void serializeMessageWhenMessagePayloadIsTooBig() throws IOException, ProtocolException {

        // given
        DummyMessage message = new DummyMessage();
        message.setNumber(123456);
        message.setString(repeatedLoremIpsum);

        expectedException.expect(ProtocolException.class);
        expectedException.expectMessage(is("Message is too long"));

        // when serializing, then expect protocol exception
        new MessageSerializer(message).serialize();
    }

    @Test
    public void serializeMessage() throws IOException, ProtocolException {

        // given
        DummyMessage message = new DummyMessage();
        message.setNumber(123456);
        message.setString(loremIpsum);

        // when serializing the message
        byte[] obtained = new MessageSerializer(message).serialize();

        // then
        assertThat(obtained, is(notNullValue()));
        assertThat(obtained.length, is(greaterThan(2)));

        // check serialization ourselves
        byte[] expectedType = message.getClass().getName().getBytes(Charset.forName("UTF-8"));
        byte[] expectedPayload;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
                objectOutputStream.writeObject(message);
                expectedPayload = outputStream.toByteArray();
            }
        }

        // check that array's length is equal to our expectations
        int expectedMessageLength = expectedType.length + expectedPayload.length + 3;
        assertThat(obtained.length, is(expectedMessageLength));

        // decode the first two bytes
        int decodedMessageLength = ((obtained[0] & 0xFF) << 8) | (obtained[1] & 0xFF);
        assertThat(decodedMessageLength, is(expectedMessageLength));

        // check expectedType identifier length
        int decodedTypeIdLength = obtained[2] & 0xFF;
        assertThat(decodedTypeIdLength, is(expectedType.length));

        // check expectedType bytes
        byte[] decodedType = new byte[expectedType.length];
        System.arraycopy(obtained, 3, decodedType, 0, expectedType.length);
        assertThat(decodedType, is(equalTo(expectedType)));

        // check expected payload
        byte[] decodedPayload = new byte[expectedPayload.length];
        System.arraycopy(obtained, 3 + expectedType.length, decodedPayload, 0, expectedPayload.length);
        assertThat(decodedPayload, is(equalTo(expectedPayload)));
    }


    private static final class DummyMessage implements Message, Serializable {

        private static final long serialVersionUID = 1L;

        private int number;
        private String string;

        @Override
        public Class<? extends Message> getType() {
            return getClass();
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getString() {
            return string;
        }

        public void setString(String string) {
            this.string = string;
        }
    }
}
