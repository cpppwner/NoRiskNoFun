package gmbh.norisknofun.game.protocol.util;

/**
 * Utility class wrapping a byte buffer, which is can grow and shrink.
 */
public class MessageBuffer {

    static final int INITIAL_BUFFER_LENGTH = 4096;

    private byte[] buffer;
    private int readOffset;
    private int writeOffset;

    public MessageBuffer() {
        buffer = new byte[INITIAL_BUFFER_LENGTH];
        readOffset = 0;
        writeOffset = 0;
    }

    public int length() {

        return writeOffset - readOffset;
    }

    public int capacity() {

        return buffer.length;
    }

    public void append(byte[] data) {

        ensureWriteCapacity(data.length);
        appendToBuffer(data);
    }

    private void ensureWriteCapacity(int length) {

        int remainingWriteCapacity = buffer.length - writeOffset;
        if (remainingWriteCapacity < length) {
            // write capacity is too low - reallocation necessary
            int additionalSizeRequired = length - (buffer.length - writeOffset);
            int newCapacity = buffer.length + (((additionalSizeRequired / INITIAL_BUFFER_LENGTH) + 1) * INITIAL_BUFFER_LENGTH);
            byte[] tmp = new byte[newCapacity];
            System.arraycopy(buffer, 0, tmp, 0, writeOffset);
            buffer = tmp;
        }
    }

    private void appendToBuffer(byte[] data) {

        System.arraycopy(data, 0, buffer, writeOffset, data.length);
        writeOffset += data.length;
    }

    public byte[] read(int length) {

        byte[] result = peek(length);
        readOffset += length;
        compactBuffer();

        return result;
    }

    public byte[] peek(int length) {

        if (length < 0) {
            throw new IllegalArgumentException("length is less than zero");
        }


        if (length > length()) {
            throw new IllegalArgumentException("length is greater than length()");
        }

        byte[] result = new byte[length];
        System.arraycopy(buffer, readOffset, result, 0, result.length);

        return result;
    }

    private void compactBuffer() {

        if (readOffset >= INITIAL_BUFFER_LENGTH) {
            System.arraycopy(buffer, readOffset, buffer, 0, length());
            writeOffset -= readOffset;
            readOffset = 0;
        }
    }

    public void clear() {

        readOffset = 0;
        writeOffset = 0;
    }
}
