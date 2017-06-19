package gmbh.norisknofun.network.socket;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;

/**
 * Client socket interface.
 */
public interface TCPClientSocket extends Closeable {

    /**
     * Read data from socket into given byte buffer.
     *
     * @param buffer Buffer where data is read into.
     * @return Number of bytes read.
     * @throws IOException If an I/O error occurs.
     */
    int read(ByteBuffer buffer) throws IOException;

    /**
     * Write data to socket from given byte buffer.
     *
     * @param buffer Buffer where data is taken from.
     * @return Number of bytes written.
     * @throws IOException If an I/O error occurs.
     */
    int write(ByteBuffer buffer) throws IOException;

    /**
     * Get remote socket address.
     *
     * @return Remote socket address.
     * @throws IOException If an I/O error occurs.
     */
    SocketAddress getRemoteAddress() throws IOException;

    /**
     * Get local socket address.
     *
     * @return Local socket address.
     * @throws IOException If an I/O error occurs.
     */
    SocketAddress getLocalAddress() throws IOException;

    /**
     * Get underlying {@link SelectableChannel}.
     *
     * @return SelectableChannel
     */
    SelectableChannel getChannel();
}
