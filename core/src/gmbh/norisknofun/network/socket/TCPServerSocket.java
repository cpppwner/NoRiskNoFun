package gmbh.norisknofun.network.socket;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;

/**
 * Server socket interface.
 */
public interface TCPServerSocket extends Closeable {

    /**
     * Accept a new connection from a client.
     *
     * @return {@link TCPClientSocket}
     * @throws IOException If an I/O error occurs.
     */
    TCPClientSocket accept() throws IOException;

    /**
     * Get underlying {@link SelectableChannel}.
     *
     * @return SelectableChannel
     */
    SelectableChannel getChannel();

    /**
     * Get server's local address to which it's bound to.
     *
     * @return {@link SocketAddress}
     * @throws IOException If an I/O error occurs.
     */
    SocketAddress getLocalAddress() throws IOException;
}
