package gmbh.norisknofun.game.protocol.util;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Unit tests for testing {@link MessageBuffer}.
 */
public class MessageBufferTests {

    private static byte[] bigData;
    private static byte[] smallData;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @BeforeClass
    public static void setupFixture() {

        bigData = new byte[4 * MessageBuffer.INITIAL_BUFFER_LENGTH + MessageBuffer.INITIAL_BUFFER_LENGTH / 4];
        new Random().nextBytes(bigData);

        smallData = new byte[MessageBuffer.INITIAL_BUFFER_LENGTH / 4];
        new Random().nextBytes(smallData);
    }

    @Test
    public void aDefaultConstructedMessageBufferHasInitialCapacity() {

        // given
        MessageBuffer target = new MessageBuffer();

        // when
        int obtained = target.capacity();

        // then
        assertThat(obtained, is(MessageBuffer.INITIAL_BUFFER_LENGTH));
    }

    @Test
    public void aDefaultConstructedMessageBufferHasZeroLength() {

        // given
        MessageBuffer target = new MessageBuffer();

        // when
        int obtained = target.length();

        // then
        assertThat(obtained, is(0));
    }

    @Test
    public void appendNullByteArrayThrowsException() {

        // given
        MessageBuffer target = new MessageBuffer();

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("data is null"));

        // when calling append with null argument, then exception is thrown
        target.append(null);
    }

    @Test
    public void appendDataAdjustsLengthAccordingly() {

        // given
        MessageBuffer target = new MessageBuffer();

        // when
        target.append(smallData);

        // then
        assertThat(target.length(), is(smallData.length));
        assertThat(target.capacity(), is(MessageBuffer.INITIAL_BUFFER_LENGTH));
    }

    @Test
    public void appendDataInSmallerChunksAdjustsLengthAccordingly() {

        // given
        MessageBuffer target = new MessageBuffer();

        // when
        for (int i = 0; i < smallData.length; i += 10) {
            byte[] data = new byte[Math.min(10, smallData.length - i)];
            System.arraycopy(smallData, i, data, 0, data.length);
            target.append(data);
        }

        // then
        assertThat(target.length(), is(smallData.length));
        assertThat(target.capacity(), is(MessageBuffer.INITIAL_BUFFER_LENGTH));
    }

    @Test
    public void appendDataAdjustsCapacityAccordingly() {

        // given
        MessageBuffer target = new MessageBuffer();

        // when
        target.append(bigData);

        // then
        assertThat(target.length(), is(bigData.length));
        assertThat(target.capacity(), is(greaterThanOrEqualTo(bigData.length)));
        assertThat(target.capacity() % MessageBuffer.INITIAL_BUFFER_LENGTH, is(0));
    }

    @Test
    public void appendDataInSmallerChunksAdjustsCapacityAccordingly() {

        // given
        MessageBuffer target = new MessageBuffer();

        // when
        for (int i = 0; i < bigData.length; i += 10) {
            byte[] data = new byte[Math.min(10, bigData.length - i)];
            System.arraycopy(bigData, i, data, 0, data.length);
            target.append(data);
        }

        // then
        assertThat(target.length(), is(bigData.length));
        assertThat(target.capacity(), is(greaterThanOrEqualTo(bigData.length)));
        assertThat(target.capacity() % MessageBuffer.INITIAL_BUFFER_LENGTH, is(0));
    }

    @Test
    public void readingLessThanZeroBytesThrowsException() {

        // given
        MessageBuffer target = new MessageBuffer();

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("length is less than zero"));

        // when/then
        target.read(-1);
    }

    @Test
    public void readingZeroBytesFromEmptyBufferWorks() {

        // given
        MessageBuffer target = new MessageBuffer();

        // when
        byte[] obtained = target.read(0);

        // then
        assertThat(obtained.length, is(0));
    }

    @Test
    public void readingFromAnEmptyBufferThrowsException() {

        // given
        MessageBuffer target = new MessageBuffer();

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("length is greater than length()"));

        // when/then
        target.read(1);
    }

    @Test
    public void readingFromFilledGivesReadData() {

        // given
        MessageBuffer target = new MessageBuffer();
        target.append(smallData);

        // when reading 0 bytes from filled buffer
        byte[] obtained = target.read(0);

        // then
        assertThat(obtained.length, is(0));
        assertThat(target.length(), is(smallData.length));
        assertThat(target.capacity(), is(MessageBuffer.INITIAL_BUFFER_LENGTH));

        // and when reading some data from filled buffer
        obtained = target.read(smallData.length / 2);

        // then
        assertThat(obtained.length, is(smallData.length / 2));
        assertThat(target.length(), is(smallData.length - (smallData.length / 2)));
        assertThat(target.capacity(), is(MessageBuffer.INITIAL_BUFFER_LENGTH));

        byte[] expectedOutput = new byte[smallData.length / 2];
        System.arraycopy(smallData, 0, expectedOutput, 0, expectedOutput.length);
        assertThat(obtained, is(equalTo(expectedOutput)));

        // and when consuming remaining data
        obtained = target.read(target.length());

        // then
        assertThat(obtained.length, is(smallData.length - (smallData.length / 2)));
        assertThat(target.length(), is(0));
        assertThat(target.capacity(), is(MessageBuffer.INITIAL_BUFFER_LENGTH));

        expectedOutput = new byte[smallData.length - (smallData.length / 2)];
        System.arraycopy(smallData, smallData.length / 2, expectedOutput, 0, expectedOutput.length);
        assertThat(obtained, is(equalTo(expectedOutput)));
    }

    @Test
    public void readingDataFromBufferDoesNotDecreaseCapacity() {

        // given
        MessageBuffer target = new MessageBuffer();
        target.append(bigData);
        int capacity = target.capacity();

        // and when reading some data from filled buffer
        byte[] obtained = target.read(bigData.length / 2);

        // then
        assertThat(obtained.length, is(bigData.length / 2));
        assertThat(target.length(), is(bigData.length - (bigData.length / 2)));
        assertThat(target.capacity(), is(capacity));

        byte[] expectedOutput = new byte[bigData.length / 2];
        System.arraycopy(bigData, 0, expectedOutput, 0, expectedOutput.length);
        assertThat(obtained, is(equalTo(expectedOutput)));

        // and when consuming remaining data
        obtained = target.read(target.length());

        // then
        assertThat(obtained.length, is(bigData.length - (bigData.length / 2)));
        assertThat(target.length(), is(0));
        assertThat(target.capacity(), is(capacity));

        expectedOutput = new byte[bigData.length - (bigData.length / 2)];
        System.arraycopy(bigData, bigData.length / 2, expectedOutput, 0, expectedOutput.length);
        assertThat(obtained, is(equalTo(expectedOutput)));
    }

    @Test
    public void peekingLessThanZeroBytesThrowsException() {

        // given
        MessageBuffer target = new MessageBuffer();

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("length is less than zero"));

        // when/then
        target.peek(-1);
    }

    @Test
    public void peekingZeroBytesFromEmptyBufferWorks() {

        // given
        MessageBuffer target = new MessageBuffer();

        // when
        byte[] obtained = target.peek(0);

        // then
        assertThat(obtained.length, is(0));
    }

    @Test
    public void peekingFromAnEmptyBufferThrowsException() {

        // given
        MessageBuffer target = new MessageBuffer();

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage(is("length is greater than length()"));

        // when/then
        target.peek(1);
    }

    @Test
    public void peekingFromFilledGivesReadData() {

        // given
        MessageBuffer target = new MessageBuffer();
        target.append(smallData);

        // when reading 0 bytes from filled buffer
        byte[] obtained = target.peek(0);

        // then
        assertThat(obtained.length, is(0));
        assertThat(target.length(), is(smallData.length));
        assertThat(target.capacity(), is(MessageBuffer.INITIAL_BUFFER_LENGTH));

        // and when peeking all data
        obtained = target.peek(smallData.length);

        // then
        assertThat(obtained.length, is(smallData.length));
        assertThat(target.length(), is(smallData.length));
        assertThat(target.capacity(), is(MessageBuffer.INITIAL_BUFFER_LENGTH));
        assertThat(obtained, is(equalTo(smallData)));
        assertThat(obtained, is(not(sameInstance(smallData))));

        // and when peeking again
        obtained = target.peek(smallData.length);

        // then
        assertThat(obtained.length, is(smallData.length));
        assertThat(target.length(), is(smallData.length));
        assertThat(target.capacity(), is(MessageBuffer.INITIAL_BUFFER_LENGTH));
        assertThat(obtained, is(equalTo(smallData)));
        assertThat(obtained, is(not(sameInstance(smallData))));
    }

    @Test
    public void clearingBufferOnlyChangesLength() {

        // given
        MessageBuffer target = new MessageBuffer();
        target.append(bigData);
        int capacity = target.capacity();

        // when
        target.clear();

        // then
        assertThat(target.length(), is(0));
        assertThat(target.capacity(), is(capacity));
    }
}
