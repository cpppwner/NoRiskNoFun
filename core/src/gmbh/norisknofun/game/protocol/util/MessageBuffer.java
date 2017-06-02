package gmbh.norisknofun.game.protocol.util;

/**
 * Utility class wrapping a byte buffer, which can grow and shrink.
 */
public final class MessageBuffer {

    /**
     * Initial capacity of internal buffer.
     */
    static final int INITIAL_BUFFER_LENGTH = 4096;

    /**
     * Internal buffer storing byte data.
     */
    private byte[] buffer;

    /**
     * Index in internal buffer from where to start next read.
     *
     * <p>
     *     Must always be in range [0, writePosition)
     * </p>
     */
    private int readPosition;

    /**
     * Index in internal buffer where to start next write operation.
     *
     * <p>
     *     Must always be in range [0, buffer.length)
     * </p>
     */
    private int writePosition;

    /**
     * Default constructor, initializing everything.
     */
    public MessageBuffer() {

        buffer = new byte[INITIAL_BUFFER_LENGTH];
        readPosition = 0;
        writePosition = 0;
    }

    /**
     * Get the length of the buffer.
     *
     * <p>
     *     The length of the buffer is defined as the number of bytes that can be read.
     * </p>
     *
     * @return Buffer length.
     */
    public int length() {

        return writePosition - readPosition;
    }

    /**
     * Get the internal buffer's capacity.
     *
     * <p>
     *     The capacity is the total number of bytes allocated.
     * </p>
     *
     * @return Internal buffer's capacity.
     */
    public int capacity() {

        return buffer.length;
    }

    /**
     * Append byte[] to internal buffer.
     *
     * <p>
     *     The internal buffer is reallocated if necessary, so the capacity might change.
     *     The {@link MessageBuffer#length()} is increased by {@code data.length}.
     * </p>
     *
     * @param data The byte[] to append.
     */
    public void append(byte[] data) {

        if (data == null) {
            throw new IllegalArgumentException("data is null");
        }

        ensureWriteCapacity(data.length);
        appendToBuffer(data);
    }

    /**
     * Ensure that at least {@code length} bytes can be appended to internal buffer - reallocating if necessary.
     *
     * @param length The number of bytes which must have space in internal buffer.
     */
    private void ensureWriteCapacity(int length) {

        int remainingWriteCapacity = buffer.length - writePosition;
        if (remainingWriteCapacity < length) {
            // write capacity is too low - reallocation necessary
            int additionalSizeRequired = length - (buffer.length - writePosition);
            int newCapacity = buffer.length + (((additionalSizeRequired / INITIAL_BUFFER_LENGTH) + 1) * INITIAL_BUFFER_LENGTH);
            byte[] tmp = new byte[newCapacity];
            System.arraycopy(buffer, 0, tmp, 0, writePosition);
            buffer = tmp;
        }
    }

    /**
     * Append given data to internal buffer, assuming enough space is available.
     *
     * @param data The data to append to the internal buffer.
     */
    private void appendToBuffer(byte[] data) {

        System.arraycopy(data, 0, buffer, writePosition, data.length);
        writePosition += data.length;
    }

    /**
     * Read given number of bytes from internal buffer and adjust {@link MessageBuffer#readPosition} accordingly.
     *
     * @param numBytes The number of bytes to read (to consume) from the internal buffer.
     *                 It must be greater than or equal to 0, otherwise an exception is thrown.
     * @return A byte[] which is exactly {@code numBytes} long containing the consumed data.
     */
    public byte[] read(int numBytes) {

        byte[] result = peek(numBytes);
        readPosition += numBytes;
        compactBuffer();

        return result;
    }

    /**
     * Read given number of bytes from internal buffer without adjusting {@link MessageBuffer#readPosition}.
     *
     * @param numBytes The number of bytes to read from the internal buffer.
     *                 It must be greater than or equal to 0, otherwise an exception is thrown.
     * @return A byte[] which is exactly {@code numBytes} long containing the read data.
     */
    public byte[] peek(int numBytes) {

        if (numBytes < 0) {
            throw new IllegalArgumentException("length is less than zero");
        }
        if (numBytes > length()) {
            throw new IllegalArgumentException("length is greater than length()");
        }

        byte[] result = new byte[numBytes];
        System.arraycopy(buffer, readPosition, result, 0, result.length);

        return result;
    }

    /**
     * Compacts the internal buffer, if necessary.
     */
    private void compactBuffer() {

        if (readPosition >= INITIAL_BUFFER_LENGTH) {
            System.arraycopy(buffer, readPosition, buffer, 0, length());
            writePosition -= readPosition;
            readPosition = 0;
        }
    }

    /**
     * Resets the {@link MessageBuffer#readPosition} and {@link MessageBuffer#writePosition}.
     *
     * <p>
     *     The internal buffer is not resized to initial length,
     *     so the {@link MessageBuffer#capacity()} stays the same.
     * </p>
     */
    public void clear() {

        readPosition = 0;
        writePosition = 0;
    }
}
