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

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Unit tests for testing the {@link Handshake} message class.
 */
public class HandshakeTests {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void passingNullMagicThrowsException() {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("protocolMagic is null or empty");

        // when/then
        new Handshake(null, "version");
    }

    @Test
    public void passingEmptyMagicThrowsException() {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("protocolMagic is null or empty");

        // when/then
        new Handshake("", "version");
    }

    @Test
    public void passingNullVersionThrowsException() {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("protocolVersion is null or empty");

        // when/then
        new Handshake("magic", null);
    }

    @Test
    public void passingEmptyVersionThrowsException() {

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("protocolVersion is null or empty");

        // when/then
        new Handshake("magic", "");
    }

    @Test
    public void fieldsAreAssignedCorrectly() {

        // given
        Handshake target = new Handshake("magic", "version");

        // then
        assertThat(target.getProtocolMagic(), is("magic"));
        assertThat(target.getProtocolVersion(), is("version"));
    }

    @Test
    public void handshakeMessageCanBeSerializedAndDeserialized() throws IOException, ProtocolException {

        // given
        Handshake handshake = new Handshake("magic", "version");

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
        assertThat(deserialized, is(instanceOf(Handshake.class)));
        assertThat(((Handshake)deserialized).getProtocolMagic(), is("magic"));
        assertThat(((Handshake)deserialized).getProtocolVersion(), is("version"));
    }
}
