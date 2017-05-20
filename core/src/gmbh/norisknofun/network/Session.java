package gmbh.norisknofun.network;

/**
 * Session for network communication.
 */
public interface Session {

    /**
     * Read data from an internal (thread safe) buffer.
     *
     * <p>
     *     Once read the data is removed from the internal buffer.
     * </p>
     *
     * @return Read data or {@code null} if no data is available.
     */
    byte[] read();

    /**
     * Write data to an internal buffer.
     *
     * <p>
     *     Writing does not directly write to the socket, but only to an
     *     internal thread safe buffer.
     * </p>
     *
     * @param data The data to write.
     */
    void write(byte[] data);

    /**
     * Gracefully close the session and flush all outgoing data.
     *
     * <p>
     *     Further write attempts will be blocked,
     *     no additional data can be written once the session is closed, but
     *     all pending data is still written to the socket, before the socket gets closed.
     * </p>
     */
    void close();

    /**
     * Terminate the session and do not flush outgoing data.
     *
     * <p>
     *     All incoming and outgoing data is discarded immediately and all further
     *     write and read attempts will not succeed.
     * </p>
     */
    void terminate();

    /**
     * Get a boolean flag indicating whether this {@link Session} is open or not.
     *
     * @return {@code true} if this {@link Session} is open, {@code false} otherwise.
     */
    boolean isOpen();

    /**
     * Get a boolean flag indicating whether this {@link Session} is closed or not.
     *
     * @return {@code true} if this {@link Session} is closed, {@code false} otherwise.
     */
    boolean isClosed();

    /**
     * Get a boolean flag indicating whether this {@link Session} is terminated or not.
     *
     * @return {@code true} if this {@link Session} is terminated, {@code false} otherwise.
     */
    boolean isTerminated();
}
