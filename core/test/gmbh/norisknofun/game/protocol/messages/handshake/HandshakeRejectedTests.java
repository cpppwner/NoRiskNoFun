package gmbh.norisknofun.game.protocol.messages.handshake;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import gmbh.norisknofun.game.networkmessages.Message;
import gmbh.norisknofun.game.protocol.MessageDeserializer;
import gmbh.norisknofun.game.protocol.MessageSerializer;
import gmbh.norisknofun.game.protocol.ProtocolException;
import gmbh.norisknofun.game.protocol.util.MessageBuffer;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for testing the {@link HandshakeRejected} class.
 */
public class HandshakeRejectedTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void passingNullVersionThrowsException() {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("serverProtocolVersion is null or empty");

        // when/then
        new HandshakeRejected(null);
    }

    @Test
    public void passingEmptyVersionThrowsException() {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("serverProtocolVersion is null or empty");

        // when/then
        new HandshakeRejected("");
    }

    @Test
    public void fieldsAreAssignedCorrectly() {

        // given
        HandshakeRejected target = new HandshakeRejected("version");

        // then
        assertThat(target.getServerProtocolVersion(), is("version"));
    }

    @Test
    public void handshakeRejctedMessageCanBeSerializedAndDeserialized() throws IOException, ProtocolException {

        // given
        HandshakeRejected handshake = new HandshakeRejected("version");

        // when
        byte[] data = new MessageSerializer(handshake).serialize();

        // then
        assertThat(data, is(notNullValue()));
        assertThat(data.length, greaterThan(0));

        // and when deserializing
        MessageBuffer buffer = new MessageBuffer();
        buffer.append(data);
        Message deserialized = new MessageDeserializer(buffer).deserialize();

        // then
        assertThat(deserialized, is(notNullValue()));
        assertThat(deserialized, is(instanceOf(HandshakeRejected.class)));
        assertThat(((HandshakeRejected)deserialized).getServerProtocolVersion(), is("version"));
    }
}
