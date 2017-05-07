package gmbh.norisknofun.network.socket;

import java.io.IOException;
import java.nio.channels.SelectableChannel;

/**
 * Server socket interface.
 */
public interface TCPServerSocket extends AutoCloseable {

    /**
     * Accept a new connection from a client.
     *
     * @return {@link TCPClientSocket}
     * @throws IOException If an I/O error occurs.
     */
    TCPClientSocket accept() throws IOException;

    /**
     * Get underlying {@link SelectableChannel}.
     */
    SelectableChannel getChannel();
}
